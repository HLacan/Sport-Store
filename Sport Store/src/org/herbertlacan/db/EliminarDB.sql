use DBSportStore2016136
go

----- Eliminar Categorias -----
create procedure sp_EliminarCategoria @CodigoCategoria int
	as
		Begin
			delete from Categorias where CodigoCategoria = @CodigoCategoria
		End
go


----- Eliminar Clientes -----
create procedure sp_EliminarCliente @CodigoCliente int
	as
		Begin
			delete from Clientes where CodigoCliente = @CodigoCliente
		End
go


----- Eliminar Compras -----
create procedure sp_EliminarCompras @NumeroDocumento int
	as
		Begin
			delete from Compras where NumeroDocumento = @NumeroDocumento
		End
go

----- Eliminar Detalle de Compra -----
create procedure sp_EliminarDetalleCompra @CodigoDetalleCompra int
	as
		Begin
			delete from DetalleCompras where CodigoDetalleCompra = @CodigoDetalleCompra
		End
go

----- Eliminar Detalle de Factura -----
create procedure sp_EliminarDetalleFactura @CodigoDetalleFactura int
	as
		Begin
			delete from DetalleFacturas where CodigoDetalleFactura = @CodigoDetalleFactura
		End
go

----- Eliminar Email Cliente -----
create procedure sp_EliminarEmailCliente @CodigoEmailCliente int
	as
		Begin
			delete from EmailClientes where CodigoEmailCliente = @CodigoEmailCliente
		End
go

----- Eliminar Email de Proveedor -----
create procedure sp_EliminarEmailProveedor @CodigoEmailProveedor int
	as
		Begin
			delete from EmailProveedores where CodigoEmailProveedor = @CodigoEmailProveedor
		End
go

----- Eliminar Facturas -----
create procedure sp_EliminarFactura @NumeroFactura int
	as
		Begin
			delete from Facturas where NumeroFactura = @NumeroFactura
		End
go

----- Eliminar Marca -----
create procedure sp_EliminarMarca @CodigoMarca int
	as
		Begin
			delete from Marcas where CodigoMarca = @CodigoMarca
		End
go

----- Eliminar Productos -----
create procedure sp_EliminarProducto @CodigoProducto int
	as
		Begin
			delete from Productos where CodigoProducto = @CodigoProducto
		End
go

----- Eliminar Proveedores -----
create procedure sp_EliminarProveedor @CodigoProveedor int
	as	
		Begin
			delete from Proveedores where CodigoProveedor = @CodigoProveedor
		End
go

----- Eliminar Tallas -----
create procedure sp_EliminarTalla @CodigoTalla int
	as
		Begin
			delete from Tallas where CodigoTalla = @CodigoTalla
		End
go

----- Eliminar Telefono Cliente -----
create procedure sp_EliminarTelefonoCliente @CodigoTelefonoCliente int
	as
		Begin
			delete from TelefonoClientes where CodigoTelefonoCliente = @CodigoTelefonoCliente
		End
go


----- Eliminar Telefono de Proveedor -----
create procedure sp_EliminarTelefonoProveedor @CodigoTelefonoProveedor int
	as	
		Begin
			delete from TelefonoProveedores where CodigoTelefonoProveedor = @CodigoTelefonoProveedor
		End
go