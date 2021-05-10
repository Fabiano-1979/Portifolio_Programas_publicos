/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.ItemPedido;
import model.Pedido;
import model.ProdutoDAO;

/**
 *
 * @author matheus
 */
public class GerenciarCarrinho extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GerenciarCarrinho</title>");
            out.println("</head>");
            out.println("<body>");
            HttpSession session = request.getSession();
            
            try {
                String op = request.getParameter("op");
                
                Pedido carrinho = (Pedido) session.getAttribute("carrinho");
                
                ArrayList<ItemPedido> itens = carrinho.getItens();
                
                if (op.equals("add")) {
                    
                    int id_produto = Integer.parseInt(request.getParameter("id_produto"));
                    int quantidade = Integer.parseInt(request.getParameter("quantidade"));

                    boolean encontrou = false;
                    
                    int index = 0;
                    
                    for (ItemPedido i : itens) {
                        if (i.getProduto().getId() == id_produto) {
                            encontrou = true;
                            break;
                        }
                        index++;
                    }

                    if (encontrou) {
                        itens.get(index).setQuantidade(quantidade + itens.get(index).getQuantidade());
                        
                    } else {
                        ItemPedido item = new ItemPedido();
                        ProdutoDAO pDAO = new ProdutoDAO();
                        item.setProduto(pDAO.carregaPorId(id_produto));
                        item.setQuantidade(quantidade);
                        itens.add(item);
                        
                    }
                    carrinho.setItens(itens);
                    session.setAttribute("carrinho", carrinho);
                    response.sendRedirect("form_catalogo_pedido.jsp?nova=n");
                    
                } else if (op.equals("del")) {
                    
                    int index = Integer.parseInt(request.getParameter("index"));
                    itens.remove(index);
                    carrinho.setItens(itens);
                    session.setAttribute("carrinho", carrinho);
                    response.sendRedirect("exibir_carrinho.jsp");
                    
                }
            } catch (Exception e) {
                out.print("Erro: " + e);
            }
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
