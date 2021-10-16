use DBSportStore2016136
go

--------------------------------------------- Ingresar y Actualizar Detalle de Compras -----------------------------------------------

drop trigger Detalle_Compra_Total_Ingresar
create trigger Detalle_Compra_Total_Ingresar
	on DetalleCompras
		for insert
			as
				Begin
					update Compras
					set Total = Total + inserted.Cantidad * inserted.CostoUnitario from Compras
					join inserted on inserted.NumeroDocumento = Compras.NumeroDocumento
					where Compras.NumeroDocumento = inserted.NumeroDocumento
				End
go

drop trigger tr_DetalleCompra_total_Actualizar
create trigger tr_DetalleCompra_total_Actualizar
	on DetalleCompras
		for update
			as
				declare @Total decimal(10,2)
				declare @totalCompra decimal(10,2)
				declare @totalOld decimal(10,2)
				declare @codigoNuevo int
				declare @codigoViejo int

				select @codigoNuevo = inserted.NumeroDocumento from Compras
				join inserted on inserted.NumeroDocumento = Compras.NumeroDocumento
				where Compras.NumeroDocumento = inserted.NumeroDocumento

				select @codigoViejo = deleted.NumeroDocumento from Compras
				join deleted on deleted.NumeroDocumento = Compras.NumeroDocumento
				where Compras.NumeroDocumento = deleted.NumeroDocumento

				if(@codigoNuevo = @codigoViejo)
					Begin

						select @Total = sum(DT.Cantidad * DT.CostoUnitario) from DetalleCompras DT
						join inserted on inserted.NumeroDocumento = DT.NumeroDocumento
						where DT.NumeroDocumento = inserted.NumeroDocumento

						update Compras
						set Total =  @Total from Compras 
						join inserted on inserted.NumeroDocumento = Compras.NumeroDocumento
						where Compras.NumeroDocumento = inserted.NumeroDocumento

						update Compras
						set Total = Total from Compras
						join inserted on inserted.NumeroDocumento = Compras.NumeroDocumento
						where Compras.NumeroDocumento = inserted.NumeroDocumento					
					End
				else
					Begin
						select @Total = sum(DT.Cantidad * DT.CostoUnitario) from DetalleCompras DT
						join inserted on inserted.NumeroDocumento = DT.NumeroDocumento
						where DT.NumeroDocumento = inserted.NumeroDocumento

						update Compras
						set Total =  @Total from Compras 
						join inserted on inserted.NumeroDocumento = Compras.NumeroDocumento
						where Compras.NumeroDocumento = inserted.NumeroDocumento

						select @totalOld = deleted.Cantidad * deleted.CostoUnitario from Compras
						join deleted on deleted.NumeroDocumento = Compras.NumeroDocumento
						where Compras.NumeroDocumento = deleted.NumeroDocumento

						select @totalCompra = C.Total from Compras C
						join inserted on inserted.NumeroDocumento = C.NumeroDocumento
						where C.NumeroDocumento = inserted.NumeroDocumento

						if(@totalCompra != 0)
							Begin
								update Compras
								set Total = @totalOld - Total from Compras DC
								join deleted on deleted.NumeroDocumento = DC.NumeroDocumento
								where DC.NumeroDocumento = deleted.NumeroDocumento
							End

						if(@totalOld - @totalCompra <= 0)
							Begin
								update Compras
								set Total = Total*-1 from Compras DC
								join deleted on deleted.NumeroDocumento = DC.NumeroDocumento
								where DC.NumeroDocumento = deleted.NumeroDocumento
							End
					End
go


--------------------------------------------- Ingresar y Actualizar Productos ----------------------------------------------------

drop trigger tr_DetalleCompras_Cantidad
create trigger tr_DetalleCompras_Cantidad
	on DetalleCompras
		for insert
			as
				update Productos
				set Existencia = Existencia + I.Cantidad from Productos P
				join inserted I on I.CodigoProducto = P.CodigoProducto
				where P.CodigoProducto = I.CodigoProducto
go

drop trigger tr_DetalleCompras_Cantidad_Actualizar
create trigger tr_DetalleCompras_Cantidad_Actualizar
	on DetalleCompras
		for update
			as
				declare @codigoNuevo int
				declare @codigoViejo int
				declare @existenciaTotal int
				declare @existenciaOld int
				declare @totalExistencia int

				select @codigoNuevo = I.CodigoProducto from Productos P
				join inserted I on I.CodigoProducto = P.CodigoProducto
				where P.CodigoProducto = I.CodigoProducto

				select @codigoViejo = D.CodigoProducto from Productos P
				join deleted D on D.CodigoProducto = P.CodigoProducto
				where P.CodigoProducto = D.CodigoProducto

				if (@codigoNuevo = @codigoViejo)
					Begin
						select @existenciaTotal = sum(DC.Cantidad) from DetalleCompras DC
						join inserted I on I.CodigoProducto = DC.CodigoProducto
						where DC.CodigoProducto = I.CodigoProducto

						update Productos
						set Existencia =  @existenciaTotal from Productos P
						join inserted I on I.CodigoProducto = P.CodigoProducto
						where P.CodigoProducto = I.CodigoProducto
					End
				else
					Begin
						select @existenciaTotal = sum(DC.Cantidad) from DetalleCompras DC
						join inserted I on I.CodigoProducto = DC.CodigoProducto
						where DC.CodigoProducto = I.CodigoProducto

						update Productos
						set Existencia =  @existenciaTotal from Productos P
						join inserted I on I.CodigoProducto = P.CodigoProducto
						where P.CodigoProducto = I.CodigoProducto

						select @existenciaOld = D.Cantidad from Productos P
						join deleted D on D.CodigoProducto = P.CodigoProducto
						where P.CodigoProducto = D.CodigoProducto

						select @totalExistencia = P.Existencia from Productos P
						join inserted I on I.CodigoProducto = P.CodigoProducto
						where P.CodigoProducto = I.CodigoProducto

						if(@totalExistencia != 0)
							Begin
								update Productos
								set Existencia = @existenciaOld - Existencia from Productos P
								join deleted D on D.CodigoProducto = P.CodigoProducto
								where P.CodigoProducto = D.CodigoProducto
							End

						if(@existenciaOld - @totalExistencia <= 0)
							Begin
								update Productos
								set Existencia = Existencia*-1 from Productos P
								join deleted D on D.CodigoProducto = P.CodigoProducto
								where P.CodigoProducto = D.CodigoProducto
							End
					End
go

------------------------------------------- Calcular y Actualizar Precio Unitario Productos -------------------------------------------
drop trigger tr_Compras_PrecioUnitario
create trigger tr_Compras_PrecioUnitario
	on DetalleCompras
		for update
			as
				declare @existencias int
				declare @cantidad int
				declare @TotalMulti decimal(10,2)
				declare @PrecioU decimal(10,2)
				declare @cantidadOld int
				declare @TotalMultiOld decimal(10,2)
				declare @PrecioUOld decimal(10,2)
				declare @CodigoOld int
				declare @CodigoNew int
				declare @diferencia decimal(10,2)

				select @CodigoNew = I.CodigoProducto from DetalleCompras DC
				join inserted I on I.CodigoProducto = DC.CodigoProducto
				where DC.CodigoProducto = I.CodigoProducto

				select @CodigoOld = D.CodigoProducto from DetalleCompras DC
				join deleted D on D.CodigoProducto = DC.CodigoProducto
				where DC.CodigoProducto = D.CodigoProducto

				if(@CodigoNew = @CodigoOld)
					Begin
						select @cantidad = sum(DC.Cantidad) from DetalleCompras DC
						join inserted I on I.CodigoProducto = DC.CodigoProducto
						where DC.CodigoProducto = I.CodigoProducto

						select @totalMulti = sum(DC.Cantidad * DC.CostoUnitario) from DetalleCompras DC
						join inserted I on I.CodigoProducto = DC.CodigoProducto
						where DC.CodigoProducto = I.CodigoProducto

						select @PrecioU = (@totalMulti)/(@cantidad)*0.25 + (@totalMulti)/(@cantidad) from DetalleCompras DC
						join inserted I on I.CodigoProducto = DC.CodigoProducto
						where DC.CodigoProducto = I.CodigoProducto

						update Productos
						set PrecioUnitario = @PrecioU from Productos P
						join inserted I on I.CodigoProducto = P.CodigoProducto
						where P.CodigoProducto = I.CodigoProducto
					End
				else
					Begin
						select @cantidad = sum(DC.Cantidad) from DetalleCompras DC
						join inserted I on I.CodigoProducto = DC.CodigoProducto
						where DC.CodigoProducto = I.CodigoProducto

						select @totalMulti = sum(DC.Cantidad * DC.CostoUnitario) from DetalleCompras DC
						join inserted I on I.CodigoProducto = DC.CodigoProducto
						where DC.CodigoProducto = I.CodigoProducto

						select @PrecioU = (@totalMulti)/(@cantidad)*0.25 + (@totalMulti)/(@cantidad) from DetalleCompras DC
						join inserted I on I.CodigoProducto = DC.CodigoProducto
						where DC.CodigoProducto = I.CodigoProducto

						update Productos
						set PrecioUnitario = @PrecioU from Productos P
						join inserted I on I.CodigoProducto = P.CodigoProducto
						where P.CodigoProducto = I.CodigoProducto

						select @cantidadOld = sum(I.Cantidad) from DetalleCompras DC
						join deleted I on I.CodigoProducto = DC.CodigoProducto
						where DC.CodigoProducto = I.CodigoProducto

						select @TotalMultiOld = sum(I.Cantidad * I.CostoUnitario) from DetalleCompras DC
						join deleted I on I.CodigoProducto = DC.CodigoProducto
						where DC.CodigoProducto = I.CodigoProducto

						select @PrecioUOld = (@TotalMultiOld)/(@cantidadOld)*0.25 + (@TotalMultiOld)/(@cantidadOld) from DetalleCompras DC
						join deleted I on I.CodigoProducto = DC.CodigoProducto
						where DC.CodigoProducto = I.CodigoProducto

						select @diferencia = (@PrecioUOld - P.PrecioUnitario) from Productos P
						join inserted I on I.CodigoProducto = P.CodigoProducto
						where P.CodigoProducto = I.CodigoProducto

						if(@diferencia <= 0)
							Begin
								select @diferencia = @diferencia*-1 from Productos P
								join deleted I on I.CodigoProducto = P.CodigoProducto
								where P.CodigoProducto = I.CodigoProducto
							End

						update Productos
						set PrecioUnitario = P.PrecioUnitario - @diferencia from Productos P
						join deleted I on I.CodigoProducto = P.CodigoProducto
						where P.CodigoProducto = I.CodigoProducto

						select @existencias = P.Existencia from Productos P
						join deleted I on I.CodigoProducto = P.CodigoProducto
						where P.CodigoProducto = I.CodigoProducto

						if(@existencias = 0)
							Begin
								update Productos
								set PrecioUnitario = 0.00 from Productos P
								join deleted I on I.CodigoProducto = P.CodigoProducto
								where P.CodigoProducto = I.CodigoProducto
							End
				End
go

---------------------------------------------------------- Actualizar Precio por Docena ----------------------------------------------------

drop trigger DetalleCompra_Actualizar_PrecioDocena
create trigger DetalleCompra_Actualizar_PrecioDocena
	on DetalleCompras
		for update
			as
				Begin
					declare @CodigoNew int
					declare @CodigoOld int
					declare @Existencias int
					declare @Diferencia decimal(10,2)
					declare @PrecioUnitario decimal(10,2)
					declare @PrecioUnitarioOld decimal(10,2)
					declare @PrecioDocena decimal(10,2)
					declare @PrecioDocenaOld decimal(10,2)

					select @CodigoNew = I.CodigoProducto from DetalleCompras DC
					join inserted I on I.CodigoProducto = DC.CodigoProducto
					where DC.CodigoProducto = I.CodigoProducto

					select @CodigoOld = D.CodigoProducto from DetalleCompras DC
					join deleted D on D.CodigoProducto = DC.CodigoProducto
					where DC.CodigoProducto = D.CodigoProducto

					if(@CodigoNew = @CodigoOld)
						Begin
							select @PrecioUnitario = P.PrecioUnitario from Productos P
							join inserted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto

							select @PrecioDocena = P.PrecioUnitario - (P.PrecioUnitario*0.05) from Productos P
							join inserted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto

							update Productos
							set PrecioDocena = @PrecioDocena from Productos P
							join inserted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto
						End
					Else
						Begin
							select @PrecioUnitario = P.PrecioUnitario from Productos P
							join inserted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto

							select @PrecioDocena = P.PrecioUnitario - (P.PrecioUnitario*0.05) from Productos P
							join inserted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto

							update Productos
							set PrecioDocena = @PrecioDocena from Productos P
							join inserted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto
							
							select @PrecioUnitarioOld = P.PrecioUnitario from Productos P
							join deleted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto

							select @PrecioDocenaOld = P.PrecioUnitario - (P.PrecioUnitario*0.05) from Productos P
							join deleted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto

							select @diferencia = (@PrecioDocenaOld - P.PrecioDocena) from Productos P
							join deleted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto
							
							if(@diferencia <= 0)
								Begin
									select @diferencia = @Diferencia * -1 from Productos P
									join deleted I on I.CodigoProducto = P.CodigoProducto
									where P.CodigoProducto = I.CodigoProducto	
								End
								
							update Productos
							set PrecioDocena = P.PrecioDocena - @diferencia from Productos P
							join deleted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto

							select @Existencias = P.Existencia from Productos P
							join deleted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto
														
							if(@Existencias = 0)
								Begin
									update Productos
									set PrecioDocena = 0.00 from Productos P
									join deleted I on I.CodigoProducto = P.CodigoProducto
									where P.CodigoProducto = I.CodigoProducto							
								End							
						End
				End

---------------------------------------------------------- Actualizar Precio por Mayor ----------------------------------------------------

create trigger DetalleCompra_Actualizar_PrecioMayor
	on DetalleCompras
		for update
			as
				Begin
					declare @CodigoNew int
					declare @CodigoOld int
					declare @Existencias int
					declare @Diferencia decimal(10,2)
					declare @PrecioUnitario decimal(10,2)
					declare @PrecioUnitarioOld decimal(10,2)
					declare @PrecioMayor decimal(10,2)
					declare @PrecioMayorOld decimal(10,2)

					select @CodigoNew = I.CodigoProducto from DetalleCompras DC
					join inserted I on I.CodigoProducto = DC.CodigoProducto
					where DC.CodigoProducto = I.CodigoProducto

					select @CodigoOld = D.CodigoProducto from DetalleCompras DC
					join deleted D on D.CodigoProducto = DC.CodigoProducto
					where DC.CodigoProducto = D.CodigoProducto

					if(@CodigoNew = @CodigoOld)
						Begin
							select @PrecioUnitario = P.PrecioUnitario from Productos P
							join inserted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto

							select @PrecioMayor = P.PrecioUnitario - (P.PrecioUnitario*0.07) from Productos P
							join inserted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto

							update Productos
							set PrecioporMayor = @PrecioMayor from Productos P
							join inserted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto
						End
					Else
						Begin
							select @PrecioUnitario = P.PrecioUnitario from Productos P
							join inserted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto

							select @PrecioMayor = P.PrecioUnitario - (P.PrecioUnitario*0.07) from Productos P
							join inserted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto

							update Productos
							set PrecioporMayor = @PrecioMayor from Productos P
							join inserted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto
							
							select @PrecioUnitarioOld = P.PrecioUnitario from Productos P
							join deleted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto

							select @PrecioMayorOld = P.PrecioUnitario - (P.PrecioUnitario*0.07) from Productos P
							join deleted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto

							select @diferencia = (@PrecioMayorOld - P.PrecioDocena) from Productos P
							join deleted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto
							
							if(@diferencia <= 0)
								Begin
									select @diferencia = @Diferencia * -1 from Productos P
									join deleted I on I.CodigoProducto = P.CodigoProducto
									where P.CodigoProducto = I.CodigoProducto	
								End
								
							update Productos
							set PrecioporMayor = P.PrecioDocena - @diferencia from Productos P
							join deleted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto

							select @Existencias = P.Existencia from Productos P
							join deleted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto
														
							if(@Existencias = 0)
								Begin
									update Productos
									set PrecioporMayor = 0.00 from Productos P
									join deleted I on I.CodigoProducto = P.CodigoProducto
									where P.CodigoProducto = I.CodigoProducto							
								End							
						End
				End

---------------------------------------------------------- Actualizar Detalle Facturas ----------------------------------------------------

drop trigger Detalle_Factura_Total_Ingresar
create trigger Detalle_Factura_Total_Ingresar
	on DetalleFacturas
		for update
			as
				Begin

					declare @Cantidad int

					select @cantidad = DF.Cantidad from DetalleFacturas DF
					join inserted I on I.CodigoProducto = DF.CodigoProducto
					where DF.CodigoProducto = I.CodigoProducto					

					if(@Cantidad < 9)
						Begin
							update Facturas
							set Total = I.Cantidad * P.PrecioUnitario from Facturas F
							join inserted I on I.NumeroFactura = F.NumeroFactura join Productos P on I.CodigoProducto = P.CodigoProducto
							where F.NumeroFactura = I.NumeroFactura

							update DetalleFacturas
							set Precio = P.PrecioUnitario from Productos P
							join inserted I on I.CodigoProducto = P.CodigoProducto
							where P.CodigoProducto = I.CodigoProducto
						End
					else
						if(@cantidad >= 9 and @cantidad < 19)
							Begin
								update Facturas
								set Total = I.Cantidad * P.PrecioDocena from Facturas F
								join inserted I on I.NumeroFactura = F.NumeroFactura join Productos P on I.CodigoProducto = P.CodigoProducto
								where F.NumeroFactura = I.NumeroFactura

								update DetalleFacturas
								set Precio = P.PrecioDocena from Productos P
								join inserted I on I.CodigoProducto = P.CodigoProducto
								where P.CodigoProducto = I.CodigoProducto
							End
				End
go

drop trigger tr_DetalleCompra_total_Actualizar
create trigger tr_DetalleCompra_total_Actualizar
	on DetalleCompras
		for update
			as
				declare @Total decimal(10,2)
				declare @totalCompra decimal(10,2)
				declare @totalOld decimal(10,2)
				declare @codigoNuevo int
				declare @codigoViejo int

				select @codigoNuevo = inserted.NumeroDocumento from Compras
				join inserted on inserted.NumeroDocumento = Compras.NumeroDocumento
				where Compras.NumeroDocumento = inserted.NumeroDocumento

				select @codigoViejo = deleted.NumeroDocumento from Compras
				join deleted on deleted.NumeroDocumento = Compras.NumeroDocumento
				where Compras.NumeroDocumento = deleted.NumeroDocumento

				if(@codigoNuevo = @codigoViejo)
					Begin

						select @Total = sum(DT.Cantidad * DT.CostoUnitario) from DetalleCompras DT
						join inserted on inserted.NumeroDocumento = DT.NumeroDocumento
						where DT.NumeroDocumento = inserted.NumeroDocumento

						update Compras
						set Total =  @Total from Compras 
						join inserted on inserted.NumeroDocumento = Compras.NumeroDocumento
						where Compras.NumeroDocumento = inserted.NumeroDocumento

						update Compras
						set Total = Total from Compras
						join inserted on inserted.NumeroDocumento = Compras.NumeroDocumento
						where Compras.NumeroDocumento = inserted.NumeroDocumento					
					End
				else
					Begin
						select @Total = sum(DT.Cantidad * DT.CostoUnitario) from DetalleCompras DT
						join inserted on inserted.NumeroDocumento = DT.NumeroDocumento
						where DT.NumeroDocumento = inserted.NumeroDocumento

						update Compras
						set Total =  @Total from Compras 
						join inserted on inserted.NumeroDocumento = Compras.NumeroDocumento
						where Compras.NumeroDocumento = inserted.NumeroDocumento

						select @totalOld = deleted.Cantidad * deleted.CostoUnitario from Compras
						join deleted on deleted.NumeroDocumento = Compras.NumeroDocumento
						where Compras.NumeroDocumento = deleted.NumeroDocumento

						select @totalCompra = C.Total from Compras C
						join inserted on inserted.NumeroDocumento = C.NumeroDocumento
						where C.NumeroDocumento = inserted.NumeroDocumento

						if(@totalCompra != 0)
							Begin
								update Compras
								set Total = @totalOld - Total from Compras DC
								join deleted on deleted.NumeroDocumento = DC.NumeroDocumento
								where DC.NumeroDocumento = deleted.NumeroDocumento
							End

						if(@totalOld - @totalCompra <= 0)
							Begin
								update Compras
								set Total = Total*-1 from Compras DC
								join deleted on deleted.NumeroDocumento = DC.NumeroDocumento
								where DC.NumeroDocumento = deleted.NumeroDocumento
							End
					End
go







