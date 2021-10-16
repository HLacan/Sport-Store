use DBSportStore2016136
go


drop trigger tr_DetalleCompras_Existencia
create trigger tr_DetalleCompras_Existencia
	on DetalleCompras
		for insert,update
			as
				Begin
				update Productos 
				set Existencia = Existencia + inserted.Cantidad from Productos
				join inserted on inserted.CodigoProducto = Productos.CodigoProducto
				where Productos.CodigoProducto = inserted.CodigoProducto
				End


create trigger tr_DetalleCompras_Total
	on DetalleCompras
		for insert
			as
				Begin
					update Compras
					set Total = Total + inserted.Cantidad * inserted.CostoUnitario from DetalleCompras
					join inserted on inserted.NumeroDocumento = DetalleCompras.NumeroDocumento
					where Compras.NumeroDocumento = inserted.NumeroDocumento
				End


drop trigger tr_DetalleCompras_Unitario
create trigger tr_DetalleCompras_Unitario
	on DetalleCompras
		for insert,update
			as
				declare @unitario decimal(10,2)
				Begin
					update Productos
					set PrecioUnitario = ((inserted.Cantidad * inserted.CostoUnitario)/(inserted.Cantidad)*0.25+((inserted.Cantidad * inserted.CostoUnitario)/(inserted.Cantidad))) from DetalleCompras
					join inserted on inserted.CodigoProducto = DetalleCompras.CodigoProducto
					where Productos.CodigoProducto = inserted.CodigoProducto
				End
go






drop trigger tr_DetalleCompras_Docena
create trigger tr_DetalleCompras_Docena
	on DetalleCompras
		for insert,update
			as
				declare @unitario int

				select @unitario = PrecioUnitario from Productos
				join inserted on inserted.CodigoProducto = Productos.CodigoProducto
				where Productos.CodigoProducto = inserted.CodigoProducto

				Begin
					update Productos
					set PrecioDocena = @unitario-(@unitario*0.05) from Productos
					join inserted on inserted.CodigoProducto = Productos.CodigoProducto
					where Productos.CodigoProducto = inserted.CodigoProducto
				End







drop trigger tr_DetalleCompras_Mayor
create trigger tr_DetalleCompras_Mayor
	on DetalleCompras
		for insert,update
			as
				declare @docena int
				declare @mayor int

				select @docena = PrecioDocena from Productos
				join inserted on inserted.CodigoProducto = Productos.CodigoProducto
				where Productos.CodigoProducto = inserted.CodigoProducto


				Begin
					update Productos
					set PrecioporMayor = @docena-(@docena*0.07) from Productos
					join inserted on inserted.CodigoProducto = Productos.CodigoProducto
					where Productos.CodigoProducto = inserted.CodigoProducto
				End





create trigger tr_DetalleCompra_Actualizar
	on DetalleCompras
		for update
			as

				declare @exist int
				select @exist = sum(C.Cantidad) from DetalleCompras C 
				join inserted on inserted.CodigoProducto = C.CodigoProducto
				where C.CodigoProducto = inserted.CodigoProducto

				Begin
				update Productos
				set Existencia =  @exist from Productos
				join inserted on inserted.CodigoProducto = Productos.CodigoProducto
				where Productos.CodigoProducto = inserted.CodigoProducto
				End