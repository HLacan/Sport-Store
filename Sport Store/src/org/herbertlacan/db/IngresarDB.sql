use DBSportStore2016136
go

----- Ingresar Categorias -----
create procedure sp_IngresarCategoria @Descripcion varchar(250)
	as
		Begin
			insert into Categorias(Descripcion) values (@Descripcion)
		End

go


----- Ingresar Cliente -----
create procedure sp_IngresarCliente @Nombre varchar(250), @Direccion varchar(250), @Nit varchar(250)
	as
		Begin
			insert into Clientes(Nombre,Direccion,Nit) values (@Nombre,@Direccion,@Nit)
		End
go


----- Ingresar Compras -----
create procedure sp_IngresarCompra @NumeroDocumento int, @Descripcion varchar(100), @Total decimal(10,2), @Fecha date, @CodigoProveedor int
	as
		Begin
			insert into Compras(NumeroDocumento,Descripcion,Total,Fecha,CodigoProveedor) values (@NumeroDocumento,@Descripcion,@Total,@Fecha,@CodigoProveedor)
		End
exec sp_IngresarCompra 987,'Compra',0.00,'01-01-2001',2
go



----- Ingresar DetalleDeCompra -----
create procedure sp_IngresarDetalleDeCompra @Cantidad int, @CostoUnitario decimal(10,2), @CodigoProducto int, @NumeroDocumento int
	as
		Begin
			insert into DetalleCompras(Cantidad,CostoUnitario,CodigoProducto,NumeroDocumento) values (@Cantidad,@CostoUnitario,@CodigoProducto,@NumeroDocumento)
		End
	exec sp_IngresarDetalleDeCompra 50,12.50,2,987
go


----- Ingresar DetalleDeFactua -----
create procedure sp_IngresarDetalleFactura @Cantidad int, @Precio decimal(10,2), @NumeroFactura int, @CodigoProducto int
	as
		Begin
			insert into DetalleFacturas(Cantidad,Precio,NumeroFactura,CodigoProducto) values (@Cantidad,@Precio,@NumeroFactura,@CodigoProducto)
		End
go

----- Ingresar Email Cliente ------
create procedure sp_IngresarEmailCliente @Descripcion varchar(250), @Email varchar(250), @CodigoCliente int
	as
		Begin
			insert into EmailClientes(Descripcion,Email,CodigoCliente) values (@Descripcion,@Email,@CodigoCliente)
		End
go


----- Ingresar EmailProveedor -----
create procedure sp_IngresarEmailProveedor @Descripcion varchar(250), @Email varchar(250), @CodigoProveedor int
	as
		Begin
			insert into EmailProveedores(Descripcion,Email,CodigoProveedor) values (@Descripcion,@Email,@CodigoProveedor)
		End
go


----- Ingresar Factura -----
create procedure sp_IngresarFactura @Fecha date, @Nit varchar(250), @Total decimal(10,2), @Estado varchar(250), @CodigoCliente int
	as
		Begin
			insert into Facturas(Fecha,Nit,Total,Estado,CodigoCliente) values (@Fecha,@Nit,@Total,@Estado,@CodigoCliente)
		End
go

----- Ingresar Marcas -----
create procedure sp_IngresarMarca @Descripcion varchar(250)
	as
		Begin
			insert into Marcas(Descripcion) values (@Descripcion)
		End
		
go

----- Ingresar Productos -----
drop procedure sp_IngresarProducto
create procedure sp_IngresarProducto @Descripcion varchar(250), @Existencia int,@Imagen varchar(250), @PrecioUnitario decimal(10,2), @PrecioDocena decimal(10,2), @PrecioporMayor decimal(10,2), @CodigoCategoria int, @CodigoMarca int, @CodigoTalla int
	as
		Begin
			insert into Productos(Descripcion,Existencia,Imagen,PrecioUnitario,PrecioDocena,PrecioporMayor,CodigoCategoria,CodigoMarca,CodigoTalla) values (@Descripcion,@Existencia,@Imagen,@PrecioUnitario,@PrecioDocena,@PrecioporMayor,@CodigoCategoria,@CodigoMarca,@CodigoTalla)
		End
		execute sp_IngresarProducto 'Nada',0,'Pc',0.0,0.00,0.00,1,1,1
go

----- Ingresar Proveedor -----
create procedure sp_IngresarProveedor @Direccion varchar(250), @Nit Varchar(250), @RazonSocial varchar(250), @PaginaWeb varchar(250)
	as
		Begin
			insert into Proveedores(Direccion,Nit,RazonSocial,PaginaWeb) values (@Direccion,@Nit,@RazonSocial,@PaginaWeb)
		End
		exec sp_IngresarProveedor '','','',''
go


----- Ingresar Talla -----
create procedure sp_IngresarTalla @Descripcion varchar(250)
	as
		Begin
			insert into Tallas(Descripcion) values (@Descripcion)
		End
		
go

----- Ingresar TelefonoCliente -----
create procedure sp_IngresarTelefonoCliente @Descripcion varchar(250), @Telefono int, @CodigoCliente int
	as
		Begin
			insert into TelefonoClientes(Descripcion,Telefono,CodigoCliente) values (@Descripcion,@Telefono,@CodigoCliente)
		End
go


----- Ingresar TelefonoProveedor -----
create procedure sp_IngresarTelefonoProveedor @Descripcion varchar(250), @Telefono int, @CodigoProveedor int
	as
		Begin
			insert into TelefonoProveedores(Descripcion,Telefono,CodigoProveedor) values (@Descripcion,@Telefono,@CodigoProveedor)
		End
go

