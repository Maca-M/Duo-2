package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


public class ProductoDao {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean registrar(Productos pro) {

        String sql = "INSERT INTO productos (articulo, nombre, descripcion, precio_compra, precio_venta) VALUES (?, ?, ?, ?, ?)";
        
        try {

            con = cn.getConexion();
            ps = con.prepareStatement(sql);

            ps.setString(1, pro.getArticulo());
            ps.setString(2, pro.getNombre());
            ps.setString(3, pro.getDescripcion());
            ps.setDouble(4, pro.getPrecioCompra());
            ps.setDouble(5, pro.getPrecioVenta());
            ps.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, e.toString());
            return false;

        }

    }

    public List ListaProductos(String valor) {

        List<Productos> listaProductos = new ArrayList();

        String sql = "SELECT * FROM productos ORDER BY estado ASC";

        String buscar = "SELECT * FROM productos WHERE articulo LIKE '%" + valor + "%' OR descripcion LIKE '%" + valor + "%'";
        
        try {

            con = cn.getConexion();

            if (valor.equalsIgnoreCase("")) {

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();

            } else {
                
                ps = con.prepareStatement(buscar);
                rs = ps.executeQuery();

            }

            while (rs.next()) {

                Productos pro = new Productos();

                pro.setId(rs.getInt("id"));
                pro.setArticulo(rs.getString("articulo"));
                pro.setNombre(rs.getString("nombre"));
                pro.setDescripcion(rs.getString("descripcion"));
                pro.setPrecioCompra(rs.getInt("precio_compra"));
                pro.setPrecioVenta(rs.getInt("precio_venta"));
                pro.setEstado(rs.getString("estado"));

                listaProductos.add(pro);

            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, e.toString());

        }

        return listaProductos;

    }

    public boolean modificar(Productos pro) {

        String sql = "UPDATE productos SET articulo = ?, nombre = ?, descripcion = ?, precio_compra = ?, precio_venta = ? WHERE id = ? ";
        
        try {

            con = cn.getConexion();
            ps = con.prepareStatement(sql);

            ps.setString(1, pro.getArticulo());
            ps.setString(2, pro.getNombre());
            ps.setString(3, pro.getDescripcion());
            ps.setDouble(4, pro.getPrecioCompra());
            ps.setDouble(5, pro.getPrecioVenta());
            ps.setInt(6, pro.getId());
            ps.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, e.toString());
            return false;

        }

    }

    public boolean accion(String estado, int id) {

        String sql = "UPDATE productos SET estado = ? WHERE id = ?";

        try {

            con = cn.getConexion();
            ps = con.prepareStatement(sql);

            ps.setString(1, estado);
            ps.setInt(2, id);
            ps.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, e.toString());
            return false;

        }

    }

    public Productos buscarCodigo(String articulo) {

        String sql = "SELECT * FROM productos WHERE articulo = ?";

        Productos pro = new Productos();

        try {

            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, articulo);
            rs = ps.executeQuery();

            if (rs.next()) {

                pro.setId(rs.getInt("id"));
                pro.setNombre(rs.getString("nombre"));
                pro.setPrecioVenta(rs.getDouble("precio_venta"));

            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, e.getMessage());

        }

        return pro;

    }
    
    public Productos buscarId(int id){

        String sql = "SELECT * FROM productos WHERE id = ?";

        Productos pro = new Productos();

        try {

            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {

                pro.setCantidad(rs.getInt("cantidad"));

            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, e.getMessage());

        }

        return pro;

    }
    
    public boolean registrarVenta(int id, double total){

        String sql = "INSERT INTO ventas (id_cliente , total) VALUES (?, ?)";

        try{

            con = cn.getConexion();
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.setDouble(2, total);
            ps.execute();

            return true;        

        } catch(SQLException e){

            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;

        }

    }
    
    public boolean registrarVentaDetalle(int idVenta,int idProducto, double precio, int cant, double subtotal, int descuento ){
        
        String sql = "INSERT INTO detalle_venta (id_venta, id_producto, precio, cantidad, subtotal, descuento) VALUES (?, ?, ?, ?, ?, ?)";
        
        try{

            con = cn.getConexion();
            ps = con.prepareStatement(sql);

            ps.setInt(1, idVenta);
            ps.setInt(2, idProducto);
            ps.setDouble(3, precio);
            ps.setInt(4, cant);
            ps.setDouble(5, subtotal);
            ps.setInt(6, descuento);
            ps.execute();

            return true;        

        } catch(SQLException e){

            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;

        }

    }
    
    public List ListaDetalle(int id) {

        List<Productos> listaPro = new ArrayList();

        String sql = "SELECT v.*, d.*, p.* FROM ventas v INNER JOIN detalle_venta d ON d.id_venta = v.id INNER JOIN productos p ON p.id = d.id_producto WHERE v.id = ?";
        
        try {

            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();            

            while (rs.next()) {

                Productos pro = new Productos();

                pro.setId(rs.getInt("id"));
                pro.setArticulo(rs.getString("articulo"));
                pro.setNombre(rs.getString("nombre"));
                pro.setDescripcion(rs.getString("descripcion"));
                pro.setCantidad(rs.getInt("cantidad"));
                pro.setPrecioVenta(rs.getDouble("precio"));
                
                listaPro.add(pro);

            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, e.toString());

        }

        return listaPro;

    }
    
    public boolean ActualizarStock(int cant, int id) {

        String sql = "UPDATE productos SET cantidad = ? WHERE id = ? ";

        try {

            con = cn.getConexion();
            ps = con.prepareStatement(sql);

            ps.setInt(1, cant);
            ps.setInt(2, id);
            ps.execute();

            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, e.toString());
            return false;

        }

    }
    
    public int getId(){

        int id = 0;

        String sql = " SELECT MAX(id) AS id FROM ventas";

        try{

            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if(rs.next()){

                id = rs.getInt("id");

            }

        }catch (SQLException e){

            System.out.println(e.getMessage());

        }

        return id;

    }
    

    public List<List<Object>> ListaProductosDia() {

        List<List<Object>> listaProductos = new ArrayList<>();

        String sql = "SELECT p.articulo, p.nombre, SUM(d.cantidad) AS cantidad " +
                     "FROM productos p " +
                     "INNER JOIN detalle_venta d ON p.id = d.id_producto " +
                     "INNER JOIN ventas v ON d.id_venta = v.id " +
                     "WHERE DATE(v.fecha) = CURDATE() " +
                     "GROUP BY p.id;";

        try {

            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {

                List<Object> producto = new ArrayList<>();

                producto.add(rs.getString("articulo"));
                producto.add(rs.getString("nombre"));
                producto.add(rs.getInt("cantidad"));

                listaProductos.add(producto);

            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, e.toString());

        }

        return listaProductos;

    }

}
    
    



