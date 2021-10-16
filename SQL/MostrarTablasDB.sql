use DBSportStore2016136
go

----- Mostrar Categorias -----
create procedure sp_MostrarCategorias
	as
		Begin
			select C.CodigoCategoria, C.Descripcion from Categorias C
		End
go

----- Mostrar Clientes ----
create procedure sp_MostrarClientes
	as
		Begin
			select CL.CodigoCliente, CL.Nombre, CL.Direccion, CL.Nit from Clientes CL
		End
go

----- Mostrar Compras -----
create procedure sp_MostrarCompras
	as
		Begin
			select CM.NumeroDocumento, CM.Descripcion, CM.Total, CM.Fecha, CM.CodigoProveedor from Compras CM
		End
go

----- Mostrar DetalleCompras -----
drop procedure sp_MostrarDetalleCompras
create procedure sp_MostrarDetalleCompras
	as
		Begin
			select DC.CodigoDetalleCompra, DC.Cantidad, DC.CostoUnitario, DC.CodigoProducto, DC.NumeroDocumento from DetalleCompras DC
		End
go


----- Mostrar DetalleFacturas -----
create procedure sp_MostrarDetalleFacturas
	as
		Begin
			select DF.CodigoDetalleFactura, DF.Cantidad, DF.Precio, DF.NumeroFactura, DF.CodigoProducto from DetalleFacturas DF
		End
go


----- Mostrar EmailClientes -----
create procedure sp_MostrarEmailClientes
	as
		Begin
			select EC.CodigoEmailCliente, EC.Descripcion, EC.Email, EC.CodigoCliente from EmailClientes EC
		End
go


----- Mostrar EmailProveedores -----
create procedure sp_MostrarEmailProveedores
	as
		Begin
			select EP.CodigoEmailProveedor, EP.Descripcion, EP.Email, EP.CodigoProveedor from EmailProveedores EP
		End
go



----- Mostrar Facturas -----
create procedure sp_MostrarFacturas
	as
		Begin
			select F.NumeroFactura, F.Fecha, F.Nit, F.Total, F.Estado, F.CodigoCliente from Facturas F
		End
go


----- Mostrar Marcas -----
create procedure sp_MostrarMarcas
	as
		Begin
			select M.CodigoMarca, M.Descripcion from Marcas M
		End
go

----- Mostrar Productos -----
create procedure sp_MostrarProductos
	as
		Begin
			select P.CodigoProducto, P.Descripcion, P.Existencia, P.Imagen, P.PrecioUnitario, P.PrecioDocena, P.PrecioporMayor, P.CodigoCategoria, P.CodigoMarca, P.CodigoTalla from Productos P
		End
go

----- Mostrar Proveedores
create procedure sp_MostrarProveedores
	as
		Begin
			select PV.CodigoProveedor, PV.Direccion, PV.Nit, PV.RazonSocial, PV.PaginaWeb from Proveedores PV
		End
go

----- Mostrar Tallas -----
create procedure sp_MostrarTallas
	as
		Begin
			select T.CodigoTalla, T.Descripcion from Tallas T
		End
go

----- Mostrar TelefonoClientes -----
create procedure sp_MostrarTelefonoClientes
	as
		Begin
			select TC.CodigoTelefonoCliente, TC.Descripcion, TC.Telefono, TC.CodigoCliente from TelefonoClientes TC
		End
go


----- Mostrar TelefonoProveedores -----
create procedure sp_MostrarTelefonoProveedores
	as
		Begin
			select TV.CodigoTelefonoProveedor, TV.Descripcion, TV.Telefono, TV.CodigoProveedor from TelefonoProveedores TV
		End
go