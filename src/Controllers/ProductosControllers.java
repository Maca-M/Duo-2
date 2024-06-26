package Controllers;

import Models.Combo;
import Models.Conexion;
import Models.ProductoDao;
import Models.Productos;
import Views.PanelAdmin;
import Views.Print;

import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ProductosControllers implements ActionListener, MouseListener, KeyListener {

    private Productos pro;
    private ProductoDao proDao;
    private PanelAdmin views;
    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableModel tmp;
    
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public ProductosControllers(Productos pro, ProductoDao proDao, PanelAdmin views) {

        this.pro = pro;
        this.proDao = proDao;
        this.views = views;

        this.views.setLocationRelativeTo(null);
        this.views.btnGenerarNV.addActionListener(this);
        this.views.btnNuevoProd.addActionListener(this);
        this.views.btnEditarProd.addActionListener(this);
        this.views.btnLimpiarProd.addActionListener(this);
        this.views.jMenuItemEliminarProd.addActionListener(this);
        this.views.jMenuItemReingresarProd.addActionListener(this);

        this.views.btnCont.addMouseListener(this);
        this.views.jTableNV.addMouseListener(this);
        this.views.jTableProd.addMouseListener(this);
        this.views.jTabbedPane1.addMouseListener(this);
        this.views.jLabelClientes.addMouseListener(this);
        this.views.jLabelFacturas.addMouseListener(this);
        this.views.jLabelProductos.addMouseListener(this);

        this.views.txtArtNV.addKeyListener(this);
        this.views.txtCantNV.addKeyListener(this);
        this.views.txtDescuento.addKeyListener(this);
        this.views.txtBuscarProd.addKeyListener(this);

        listarProductos();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == views.btnNuevoProd) {

            if (views.txtArtProd.getText().equals("")
                    || views.txtNombreProd.getText().equals("")
                    || views.txtDescripcionProd.getText().equals("")
                    || views.txtCompraProd.getText().equals("")
                    || views.txtVentaProd.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");

            } else {

                pro.setArticulo(views.txtArtProd.getText());
                pro.setNombre(views.txtNombreProd.getText());
                pro.setDescripcion(views.txtDescripcionProd.getText());
                pro.setPrecioCompra(Double.parseDouble(views.txtCompraProd.getText()));
                pro.setPrecioVenta(Double.parseDouble(views.txtVentaProd.getText()));

                if (proDao.registrar(pro)) {

                    listarProductos();
                    limpiar();
                    JOptionPane.showMessageDialog(null, "Producto registrado");

                } else {

                    JOptionPane.showMessageDialog(null, "Error al registrar");

                }

            }

        } else if (e.getSource() == views.btnEditarProd) {

            if (views.txtIdProd.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Seleccione una fila");

            } else {

                if (views.txtArtProd.getText().equals("")
                        || views.txtNombreProd.getText().equals("")
                        || views.txtDescripcionProd.getText().equals("")
                        || views.txtCompraProd.getText().equals("")
                        || views.txtVentaProd.getText().equals("")) {

                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");

                } else {

                    pro.setArticulo(views.txtArtProd.getText());
                    pro.setNombre(views.txtNombreProd.getText());
                    pro.setDescripcion(views.txtDescripcionProd.getText());
                    pro.setPrecioCompra(Double.parseDouble(views.txtCompraProd.getText()));
                    pro.setPrecioVenta(Double.parseDouble(views.txtVentaProd.getText()));
                    pro.setId(Integer.parseInt(views.txtIdProd.getText()));

                    if (proDao.modificar(pro)) {

                        listarProductos();
                        limpiar();
                        JOptionPane.showMessageDialog(null, "Producto Modificado");

                    } else {

                        JOptionPane.showMessageDialog(null, "Error al Modificar");

                    }

                }

            }

        } else if (e.getSource() == views.btnLimpiarProd) {

            limpiar();

        } else if (e.getSource() == views.jMenuItemReingresarProd) {

            if (views.txtIdProd.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Selecciona una fila");

            } else {

                int id = Integer.parseInt(views.txtIdProd.getText());

                if (proDao.accion("Activo", id)) {

                    // limpiarTable();
                    listarProductos();
                    limpiar();
                    JOptionPane.showMessageDialog(null, "Producto reingresado");
                    
                } else {

                    JOptionPane.showMessageDialog(null, "Error al reingresar producto");

                }

            }

        } else if (e.getSource() == views.jMenuItemEliminarProd) {

            if (views.txtIdProd.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Selecciona una fila");

            } else {

                int id = Integer.parseInt(views.txtIdProd.getText());

                if (proDao.accion("Inactivo", id)) {

                    // limpiarTable();
                    listarProductos();
                    limpiar();
                    JOptionPane.showMessageDialog(null, "Producto eliminado");

                } else {

                    JOptionPane.showMessageDialog(null, "Error al eliminar producto");

                }

            }

        } else if (e.getSource() == views.btnGenerarNV){

            insertarVenta();

        }

    }

    public void listarProductos() {
        
        limpiarTable();

        List<Productos> lista = proDao.ListaProductos(views.txtBuscarProd.getText());
        modelo = (DefaultTableModel) views.jTableProd.getModel();
        Object[] ob = new Object[7];

        for (int i = 0; i < lista.size(); i++) {

            ob[0] = lista.get(i).getId();
            ob[1] = lista.get(i).getArticulo();
            ob[2] = lista.get(i).getNombre();
            ob[3] = lista.get(i).getDescripcion();
            ob[4] = lista.get(i).getPrecioCompra();
            ob[5] = lista.get(i).getPrecioVenta();
            ob[6] = lista.get(i).getEstado();

            modelo.addRow(ob);

        }

        views.jTableProd.setModel(modelo);
        
    }

    public void limpiarTable() {

        modelo.setRowCount(0);

    }
    
    public void limpiarTableDetalle() {

        tmp.setRowCount(0);

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getSource() == views.jTableProd) {

            int fila = views.jTableProd.rowAtPoint(e.getPoint());

            views.txtIdProd.setText(views.jTableProd.getValueAt(fila, 0).toString());
            views.txtArtProd.setText(views.jTableProd.getValueAt(fila, 1).toString());
            views.txtNombreProd.setText(views.jTableProd.getValueAt(fila, 2).toString());
            views.txtDescripcionProd.setText(views.jTableProd.getValueAt(fila, 3).toString());
            views.txtCompraProd.setText("" + views.jTableProd.getValueAt(fila, 4).toString());
            views.txtVentaProd.setText("" + views.jTableProd.getValueAt(fila, 5).toString());

        } else if (e.getSource() == views.jLabelProductos || (e.getSource() == views.jTabbedPane1 && views.jTabbedPane1.getSelectedIndex() == 2)) {
            
            views.jTabbedPane1.setSelectedIndex(2);
            listarProductos();

        } else if (e.getSource() == views.jLabelFacturas) {

            views.jTabbedPane1.setSelectedIndex(3);
        
        } else if (e.getSource() == views.jLabelClientes) {

            views.jTabbedPane1.setSelectedIndex(1);
            
        } else if (e.getSource() == views.jTableNV) {

            int fila = views.jTableNV.rowAtPoint(e.getPoint());
            
            views.txtIdNV.setText(views.jTableNV.getValueAt(fila, 0).toString());
            views.txtArtNV.setText(views.jTableNV.getValueAt(fila, 1).toString());
            views.txtNombreNV.setText(views.jTableNV.getValueAt(fila, 2).toString());
            views.txtCantNV.setText(views.jTableNV.getValueAt(fila, 3).toString());
            views.txtPrecioNV.setText("" + views.jTableNV.getValueAt(fila, 4).toString());
            views.txtTotalNV.setText("" + views.jTableNV.getValueAt(fila, 5).toString());
            tmp.removeRow(fila);

        } else if (e.getSource() == views.btnCont || (e.getSource() == views.jTabbedPane1 && views.jTabbedPane1.getSelectedIndex() == 4)) {
           
            listarCont();

        }
    
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    private void limpiar() {

        views.txtArtProd.setText("");
        views.txtNombreProd.setText("");
        views.txtDescripcionProd.setText("");
        views.txtCompraProd.setText("");
        views.txtVentaProd.setText("");
        views.txtIdProd.setText("");

    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getSource() == views.txtArtNV) {

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                if (views.txtArtNV.getText().equals("")) {

                    JOptionPane.showMessageDialog(null, "Ingrese codigo de articulo");

                } else {
                
                    String art = views.txtArtNV.getText();
                    pro = proDao.buscarCodigo(art);

                    views.txtIdNV.setText("" + pro.getId());
                    views.txtNombreNV.setText(pro.getNombre());
                    views.txtPrecioNV.setText("" + pro.getPrecioVenta());

                    if (e.getSource() == views.txtArtNV) {

                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                            views.txtCantNV.requestFocus();

                        }

                    }

                }

            }

        } else if (e.getSource() == views.txtCantNV) {

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                agregarTemp();
                
            }

        }else if (e.getSource() == views.txtDescuento){

            if (e.getKeyCode() == KeyEvent.VK_ENTER){

                calcularVenta();

            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getSource() == views.txtBuscarProd) {

            listarProductos();

        } else if (e.getSource() == views.txtCantNV) {

            int cantidad;
            
            if (views.txtCantNV.getText().equals("")) {

                double precio = Double.parseDouble(views.txtPrecioNV.getText());
                views.txtTotalNV.setText("" + precio);

            } else {

                cantidad = Integer.parseInt(views.txtCantNV.getText());
                double precio = Double.parseDouble(views.txtPrecioNV.getText());
                views.txtTotalNV.setText("" + (cantidad * precio));

            }
       
        }

    }

    private void limpiarCampos(){

        views.txtArtNV.setText("");
        views.txtIdNV.setText("");
        views.txtNombreNV.setText("");
        views.txtCantNV.setText("");
        views.txtPrecioNV.setText("");
        views.txtTotalNV.setText("");

    }

    private void agregarTemp() {

        int id          = Integer.parseInt(views.txtIdNV.getText());
        int art         = Integer.parseInt(views.txtArtNV.getText());
        String nombre   = views.txtNombreNV.getText();
        double precio   = Double.parseDouble(views.txtPrecioNV.getText());
        int cant        = Integer.parseInt(views.txtCantNV.getText());
        double total    = Double.parseDouble(views.txtTotalNV.getText());

        if (cant > 0) {
            
            tmp = (DefaultTableModel) views.jTableNV.getModel();
            ArrayList lista = new ArrayList();
            int item = 1;

            lista.add(item);
            lista.add(id);
            lista.add(art);
            lista.add(nombre);
            lista.add(cant);
            lista.add(precio);
            lista.add(total);

            Object[] obj = new Object[6];

            obj[0] = lista.get(1);
            obj[1] = lista.get(2);
            obj[2] = lista.get(3);
            obj[3] = lista.get(4);
            obj[4] = lista.get(5);
            obj[5] = lista.get(6);

            tmp.addRow(obj);
            views.jTableNV.setModel(tmp);
            limpiarCampos();
            calcularVenta();
            views.txtArtNV.requestFocus();

        }

    }
           
    private void calcularVenta(){

        double subtotal = 0.00;
        double total;
        double desc = 1 - (Double.parseDouble(views.txtDescuento.getText()) / 100);

        int numFila = views.jTableNV.getRowCount();

        for (int i = 0; i < numFila; i++){

            subtotal = subtotal + (double) views.jTableNV.getValueAt(i,5);

        }

        total = subtotal * desc;

        views.txtTotal.setText(""+total);

    }
    
    public void listarCont() {
        
        List<List<Object>> lista = proDao.ListaProductosDia();
        modelo = (DefaultTableModel) views.jTableCont.getModel();
        modelo.setRowCount(0);
        
        for (int i = 0; i < lista.size(); i++) {

            Object[] ob = lista.get(i).toArray();
            modelo.addRow(ob);

        }

        views.jTableCont.setModel(modelo);
        
    }
    
    private void insertarVenta(){

        Combo cliente = (Combo) views.seleccionarCliente.getSelectedItem();
        int idCliente = cliente.getId();
        double total = Double.parseDouble(views.txtTotal.getText());

        if(proDao.registrarVenta(idCliente,  total)){

            int id = proDao.getId();

            for (int i = 0; i < views.jTableNV.getRowCount(); i++) {
                
                int idProducto = Integer.parseInt(views.jTableNV.getValueAt(i, 0).toString());
                int cantidad = Integer.parseInt(views.jTableNV.getValueAt(i, 3).toString());
                double precio = Double.parseDouble(views.jTableNV.getValueAt(i, 4).toString());
                double subtotal = precio * cantidad;
                int desc = Integer.parseInt(views.txtDescuento.getText());

                proDao.registrarVentaDetalle(id, idProducto, precio, cantidad, subtotal, desc);
                pro = proDao.buscarId(idProducto);
                listarCont();

            }

            limpiarTableDetalle();
            JOptionPane.showMessageDialog(null, "venta generada");
            Print p = new Print(id);
            
            p.setVisible(true);

        }
        
    }

}
