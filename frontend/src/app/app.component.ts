import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from './services/user.service';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;
  @ViewChild(MatSort) sort: MatSort | undefined;

  users: any = [];
  dataSource: MatTableDataSource<any> = new MatTableDataSource(this.users);
  displayedColumns: string[] = ['id', 'nombre', 'apellido', 'edad', 'escuela', 'carrera', 'anios_cursados', 'options'];

  panelOpenState = false;
  public userForm: FormGroup = new FormGroup({});
  
  constructor(
    public fb: FormBuilder,
    public userService: UserService,
  ) {

  }

  ngOnInit(): void {
    this.userForm = this.fb.group({
      id: [''],
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      edad: ['', Validators.required],
      escuela: ['', Validators.required],
      carrera: ['', Validators.required],
      anios_cursados: ['', Validators.required],
    });

    this.userService.getAllUsers().subscribe(resp => {
      this.users = resp;
      this.setDataAndPagination();
    },
      error => { console.error(error) }
    );
  }

  ngAfterViewInit(): void {
    this.setDataAndPagination();
  }

  createUser(): void {
    this.userService.createUser(this.userForm.value).subscribe(resp => {
      this.userForm.reset();
      this.userForm.setErrors(null);
      this.users=this.users.filter((user: any)=> resp.id!==user.id);
      this.users.push(resp);
      this.setDataAndPagination();
    },
      error => { console.error(error) }
    )
  }

  deleteUser(user: any){
    this.userService.deleteUser(user.id).subscribe(resp=>{
      if(resp){
        this.users=this.users.filter((u: any)=> user.id!==u.id);
        this.setDataAndPagination();
      }
    })
  }

  updateUser() {
    this.userService.updateUser(this.userForm.value).subscribe(resp => {
      this.userForm.reset();
      this.userForm.setErrors(null);
      this.users=this.users.filter((u: any)=> resp.id!==u.id);
      this.users.push(resp);
      this.setDataAndPagination();
    },
      error => { console.error(error) }
    )
  }

  editForm(user: any){
    this.userForm.setValue({
      id: user.id,
      nombre: user.nombre ,
      apellido: user.apellido ,
      edad: user.edad,
      escuela: user.escuela,
      carrera: user.carrera,
      anios_cursados: user.anios_cursados,
    });
    this.panelOpenState = !this.panelOpenState;
  }

  setDataAndPagination(){
    this.dataSource = new MatTableDataSource(this.users);
    this.dataSource.paginator = this.paginator!;
    this.dataSource.sort = this.sort!;
  }
}
