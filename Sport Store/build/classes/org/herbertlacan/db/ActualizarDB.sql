use DBSportStore2016136
go

----- Actualizar Categoria -----
create procedure sp_ActualizarCategoria @Descripcion varchar(250), @CodigoCategoria int
	as
		Begin
			update Categorias
			set Descripcion = @Descripcion
			where CodigoCategoria = @CodigoCategoria
		End
go


----- Actualizar Cliente -----
create procedure sp_ActualizarCliente @CodigoCliente int, @Nombre varchar(250), @Direccion varchar(250), @Nit varchar(100)
	as
		Begin
			update Clientes
			set Nombre = @Nombre, Direccion = @Direccion, Nit = @Nit
			where CodigoCliente = @CodigoCliente
		End
go


----- Actualizar Compra -----
create procedure sp_ActualizarCompra @NumeroDocumento int, @Descripcion varchar(250), @Total decimal(10,2), @Fecha varchar(250), @CodigoProveedor int
	as
		Begin
			update Compras
			set Descripcion = @Descripcion, Total=@Total, Fecha = @Fecha, CodigoProveedor = @CodigoProveedor
			where NumeroDocumento = @NumeroDocumento
		End
go


----- Actualizar DetalleCompra -----
create procedure sp_ActualizarDetalleCompra @CodigoDetalleCompra int, @Cantidad int, @CostoUnitario decimal(10,2), @CodigoProducto int, @NumeroDocumento int
	as
		Begin
			update DetalleCompras
			set Cantidad = @Cantidad, CostoUnitario = @CostoUnitario, CodigoProducto = @CodigoProducto, NumeroDocumento = @NumeroDocumento
			where CodigoDetalleCompra = @CodigoDetalleCompra
		End 
		exec sp_ActualizarDetalleCompra 1,20,10.00,2,987

go

----- Actualizar DetalleFactura -----
create procedure sp_ActualizarDetalleFactura @CodigoDetalleFactura int, @Cantidad int, @Precio decimal(10,2), @CodigoProducto int, @NumeroFactura int
	as
		Begin
			update DetalleFacturas
			set Cantidad = @Cantidad, Precio = @Precio, CodigoProducto = @CodigoProducto, NumeroFactura = @NumeroFactura
			where CodigoDetalleFactura = @CodigoDetalleFactura
		End
go

----- Actualizar Email Cliente -----
create procedure sp_ActualizarEmailCliente @CodigoEmailCliente int, @Descripcion varchar(250), @Email varchar(250), @CodigoCliente int
	as
		Begin
			update EmailClientes
			set Descripcion = @Descripcion, Email = @Email, CodigoCliente = @CodigoCliente
			where CodigoEmailCliente = @CodigoEmailCliente
		End
go

----- Actualizar Email Proveedor -----
create procedure sp_ActualizarEmailProveedor @CodigoEmailProveedor int, @Descripcion varchar(250), @Email varchar(250), @CodigoProveedor int
	as
		Begin
			update EmailProveedores
			set Descripcion = @Descripcion, Email = @Email, CodigoProveedor = @CodigoProveedor
			where CodigoEmailProveedor = @CodigoEmailProveedor
		End
go

----- Actualizar Factura -----
create procedure sp_ActualizarFactura @NumeroFactura int, @Fecha varchar(250), @Nit varchar(250), @Total decimal(10,2), @Estado varchar(250), @CodigoCliente int
	as
		Begin
			update Facturas
			set Fecha = @Fecha, Nit = @Nit, Total = @Total, Estado = @Estado, CodigoCliente = @CodigoCliente
			where NumeroFactura = @NumeroFactura
		End
go


----- Actualizar Marcas -----
create procedure sp_ActualizarMarca @CodigoMarca int, @Descripcion varchar(250)
as
	Begin
		update Marcas
		set Descripcion = @Descripcion
		where CodigoMarca = @CodigoMarca
	End
go

----- Actualizar Productos -----
create procedure sp_ActualizarProducto @CodigoProducto int, @Descripcion varchar(250), @Existencia int, @Imagen varchar(250), @PrecioUnitario decimal(10,2), @PrecioDocena decimal(10,2), @PrecioporMayor decimal(10,2), @CodigoCategoria int, @CodigoMarca int, @CodigoTalla int
		as
			Begin
				update Productos
				set Descripcion = @Descripcion, Existencia = @Existencia, Imagen = @Imagen, PrecioUnitario = @PrecioUnitario, PrecioDocena = @PrecioDocena, PrecioporMayor = @PrecioporMayor, CodigoCategoria = @CodigoCategoria, CodigoMarca = @CodigoMarca, CodigoTalla = @CodigoTalla
				where CodigoProducto = @CodigoProducto
			End
go

----- Actualizar Proveedores -----
create procedure sp_ActualizarProveedor @CodigoProveedor int, @Direccion varchar(250), @Nit varchar(250), @RazonSocial varchar(250), @PaginaWeb varchar(250)
	as
		Begin
			update Proveedores
			set Direccion = @Direccion, Nit = @Nit, RazonSocial = @RazonSocial, PaginaWeb = @PaginaWeb
			where CodigoProveedor = @CodigoProveedor
		End
go

----- Actualizar Tallas -----
create procedure sp_ActualizarTallas @CodigoTalla int, @Descripcion varchar(250)
	as
		Begin
			update Tallas
			set Descripcion = @Descripcion
			where CodigoTalla = @CodigoTalla
		End
go

----- Actualizar Telefono Clientes -----
create procedure sp_ActualizarTelefonoCliente @CodigoTelefonoCliente int, @Descripcion varchar(250), @Telefono int, @CodigoCliente int
	as
		Begin
			update TelefonoClientes
			set Descripcion = @Descripcion, Telefono = @Telefono, CodigoCliente = @CodigoCliente
			where CodigoTelefonoCliente = @CodigoTelefonoCliente
		End
go

----- Actualizar Telefono Proveedores -----
create procedure sp_ActualizarTelefonoProveedor @CodigoTelefonoProveedor int, @Descripcion varchar(250), @Telefono int, @CodigoProveedor int
	as
		Begin
			update TelefonoProveedores
			set Descripcion = @Descripcion, Telefono = @Telefono, CodigoProveedor = @CodigoProveedor
			where CodigoTelefonoProveedor = @CodigoTelefonoProveedor
		End
go