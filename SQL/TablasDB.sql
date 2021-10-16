create database DBSportStore2016136
go
use DBSportStore2016136
go


----- Creacion de la tabla Productos -----
create table Productos
	(
		CodigoProducto int primary key identity(1,1),
		Descripcion varchar(250),
		Existencia int default 0,
		Imagen varchar(250),
		PrecioUnitario decimal(10,2) default 0.0,
		PrecioDocena decimal(10,2) default 0.0,
		PrecioporMayor decimal(10,2) default 0.0,
		CodigoCategoria int,
		CodigoMarca int,
		CodigoTalla int,
		Constraint FK_Productos_Categorias foreign key(CodigoCategoria) references Categorias(CodigoCategoria),
		constraint FK_Productos_Marcas foreign key(CodigoMarca) references Marcas(CodigoMarca),
		constraint FK_Productos_Tallas foreign key(CodigoTalla) references Tallas(CodigoTalla),
	)
go

----- Creacion de la tabla Categorias -----
create table Categorias
	(
		CodigoCategoria int primary key identity(1,1),
		Descripcion varchar(250),
	)
go
	

----- Creacion de la tabla Marcas -----
create table Marcas
	(
		CodigoMarca int primary key identity(1,1),
		Descripcion varchar(250),
	)
go


----- Creacion de la tabla Tallas -----
create table Tallas
	(
		CodigoTalla int primary key identity(1,1),
		Descripcion varchar(250),
	)
go


----- Creacion de la tabla Proveedores -----
create table Proveedores
	(
		CodigoProveedor int primary key identity(1,1),
		Direccion varchar(250),
		Nit varchar(250),
		RazonSocial varchar(250),
		PaginaWeb varchar(250),
	)
go


----- Creacion de la tabla Telefono Proveedores -----
create table TelefonoProveedores
	(
		CodigoTelefonoProveedor int primary key identity(1,1),
		Descripcion varchar(250),
		Telefono int,
		CodigoProveedor int,
		Constraint FK_TelefonoProveedores_Proveedores foreign key (CodigoProveedor) references Proveedores(CodigoProveedor),
	)
go

----- Creacion de la tabla Email Proveedores -----
create table EmailProveedores
	(
		CodigoEmailProveedor int primary key identity(1,1),
		Descripcion varchar(250),
		Email varchar(250),
		CodigoProveedor int,
		Constraint FK_EmailProveedores_Proveedores foreign key (CodigoProveedor) references Proveedores(CodigoProveedor),
	)
go


----- Creacion de la tabla Compras -----
create table Compras
	(
		NumeroDocumento int primary key,
		Descripcion varchar(250),
		Total decimal(10,2),
		Fecha varchar(250),
		CodigoProveedor int,
		Constraint FK_Compras_Proveedores foreign key (CodigoProveedor) references Proveedores(CodigoProveedor),
	)
go


----- Creacion de la tabla DetalleCompras
create table DetalleCompras
	(
		CodigoDetalleCompra int primary key identity(1,1),
		Cantidad int,
		CostoUnitario decimal(10,2),
		CodigoProducto int,
		NumeroDocumento int,
		constraint FK_DetalleCompras_Productos foreign key (CodigoProducto) references Productos(CodigoProducto),
		constraint fk_DetalleCompras_Compras foreign key (NumeroDocumento) references Compras(NumeroDocumento),
	)
go	


-----Creacion de la tabla Clientes -----
create table Clientes
	(
		CodigoCliente int primary key identity(1,1),
		Nombre varchar(250),
		Direccion varchar(250),
		Nit varchar(250),
	)
go


----- Creacion de la tabla EmailClientes -----
create table EmailClientes
	(
		CodigoEmailCliente int primary key identity(1,1),
		Descripcion varchar(250),
		Email varchar(250),
		CodigoCliente int,
		Constraint FK_EmailClientes_Clientes foreign key (CodigoCliente) references Clientes(CodigoCliente),
	)
go


----- Creacion de la tabla TelefonoClientes -----
create table TelefonoClientes
	(
		CodigoTelefonoCliente int primary key identity(1,1),
		Descripcion varchar(250),
		Telefono int,
		CodigoCliente int,
		Constraint FK_TelefonoClientes_Clientes foreign key(CodigoCliente) references Clientes(CodigoCliente),
	)
go


----- Creacion de la tabla Factuas -----
create table Facturas
	(
		NumeroFactura int primary key identity(1,1),
		Fecha varchar(250),
		Nit varchar(250),
		Total decimal(10,2),
		Estado varchar(250),
		CodigoCliente int,
		Constraint FK_Facturas_Clientes foreign key (CodigoCliente) references Clientes(CodigoCliente),
	)
go


----- Creacion de la tabla DetalleFacturas -----
create table DetalleFacturas
	(
		CodigoDetalleFactura int primary key identity(1,1),
		Cantidad int,
		Precio decimal(10,2),
		NumeroFactura int,
		CodigoProducto int,
		Constraint FK_DetalleFacturas_Facturas foreign key (NumeroFactura) references Facturas(NumeroFactura),
		Constraint FK_DetalleFacturas_Productos foreign key (CodigoProducto) references Productos(CodigoProducto),
	)
go


