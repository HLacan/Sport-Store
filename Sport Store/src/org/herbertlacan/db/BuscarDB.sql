use DBSportStore2016136
go
----- Buscar Categoria -----
create procedure sp_BuscarCategoria @CodigoCategoria int
	as
		Begin
			select C.CodigoCategoria, C.Descripcion from Categorias C
			where CodigoCategoria = @CodigoCategoria
		End
go


----- Buscar Cliente -----
create procedure sp_BuscarCliente @CodigoCliente int
	as
		Begin
			select C.CodigoCliente, C.Nombre, C.Direccion, C.Nit from Clientes C
			where CodigoCliente = @CodigoCliente
		End
go


----- Buscar Compras -----
create procedure sp_BuscarCompra @NumeroDocumento int
	as
		Begin
			select CM.NumeroDocumento, CM.Descripcion, CM.Total, CM.Fecha, CM.CodigoProveedor from Compras CM
			where NumeroDocumento = @NumeroDocumento
		End
go

----- Buscar DetalleCompras -----
create procedure sp_BuscarDetalleCompras @CodigoDetalleCompra int
	as
		Begin
			select DC.CodigoDetalleCompra, DC.Cantidad, DC.CostoUnitario, DC.CodigoProducto, DC.NumeroDocumento from DetalleCompras DC
			where CodigoDetalleCompra = @CodigoDetalleCompra
		End
go

----- Buscar DetalleFacturas -----
create procedure sp_BuscarDetalleFacturas @CodigoDetalleFactura int
	as
		Begin
			select DF.CodigoDetalleFactura, DF.Cantidad, DF.Precio, DF.CodigoProducto, DF.NumeroFactura from DetalleFacturas DF
			where CodigoDetalleFactura = @CodigoDetalleFactura
		End
go

----- Buscar EmailCliente -----
create procedure sp_BuscarEmailCliente @CodigoEmailCliente int
	as
		Begin
			select EC.CodigoEmailCliente, EC.Descripcion, EC.Email, EC.CodigoCliente from EmailClientes EC
			where CodigoEmailCliente = @CodigoEmailCliente
		End
go

----- Buscar Email Proveedor -----
create procedure sp_BuscarEmailProveedor @CodigoEmailProveedor int
	as
		Begin
			select EP.CodigoEmailProveedor, EP.Descripcion, EP.Email, EP.CodigoProveedor from EmailProveedores EP
			where CodigoEmailProveedor = @CodigoEmailProveedor
		End
go

----- Buscar Facturas -----
create procedure sp_BuscarFacturas @NumeroFactura int
	as
		Begin
			select F.NumeroFactura, F.Fecha, F.Nit, F.Total, F.Estado, F.CodigoCliente from Facturas F
			where NumeroFactura = @NumeroFactura
		End
go

----- Buscar Marcas -----
create procedure sp_BuscarMarca @CodigoMarca int
	as
		Begin
			select C.CodigoMarca, C.Descripcion from Marcas C
			where CodigoMarca = @CodigoMarca
		End
go

----- Buscar Productos -----
create procedure sp_BuscarProducto @CodigoProducto int
	as
		Begin
			select P.CodigoProducto, P.Descripcion, P.Existencia, P.Imagen, P.PrecioUnitario, P.PrecioDocena, P.PrecioporMayor, P.CodigoCategoria, P.CodigoMarca, P.CodigoTalla from Productos P
			where CodigoProducto = @CodigoProducto
		End
go

----- Buscar Proveedor -----
create procedure sp_BuscarProveedor @CodigoProveedor int
	as
		Begin
			select PV.CodigoProveedor, PV.Direccion, PV.Nit, PV.RazonSocial, PV.PaginaWeb from Proveedores PV
			where CodigoProveedor = @CodigoProveedor
		End
go		

----- Buscar Talla -----
create procedure sp_BuscarTalla @CodigoTalla int
	as
		Begin
			select C.CodigoTalla, C.Descripcion from Tallas C
			where CodigoTalla = @CodigoTalla
		End
go

----- Buscar Telefono Clientes -----
create procedure sp_BuscarTelefonoCliente @CodigoTelefonoCliente int
	as
		Begin
			select TC.CodigoTelefonoCliente, TC.Descripcion, TC.Telefono, TC.CodigoCliente from TelefonoClientes TC
			where CodigoTelefonoCliente = @CodigoTelefonoCliente
		End
go


----- Buscar Telefono Proveedores -----
create procedure sp_BuscarTelefonoClientes @CodigoTelefonoProveedor int
	as
		Begin
			select TP.CodigoTelefonoProveedor, TP.Descripcion, TP.Telefono, TP.CodigoProveedor from TelefonoProveedores TP
			where CodigoTelefonoProveedor = @CodigoTelefonoProveedor
		End
go