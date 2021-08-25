/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.m3techos.dal;

import java.sql.*;

/**
 *
 * @author m3tech
 */
public class ModuloConexao {

    //metodo para conectar no banco

    public static Connection conector() {
        Connection conexao = null;
        String driver = "com.mysql.jdbc.Driver"; //caminho do driver jdbc
        String url = "jdbc:mysql://localhost:3306/dbosm3tech";//caminho banco de dados do sistema
        String user = "root";//usuario de acesso ao banco de dados
        String password = "";//senha de acesso ao banco de dados
        
        //Estabelecer conexao com o banco de dados
        try {
            Class.forName(driver);//carrega o driver indicado
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            //System.out.println(e);
            return null;
        }
    }

}
