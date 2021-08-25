/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.m3techos.telas;

import java.sql.*;
import br.com.m3techos.dal.ModuloConexao;
import java.util.HashMap;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author m3tech
 */
public class TelaOs extends javax.swing.JInternalFrame {

    Connection conexao = null;//recebe a conexao
    PreparedStatement pst = null;//usada para executar instrucoes sql
    ResultSet rs = null;//usada para executar instrucoes sql

    private String tipo;

    /**
     * Creates new form TelaOs
     */
    public TelaOs() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    private void pesquisar_cliente() {
        String sql = "select idcliente as ID, nomecliente as Nome, telcliente as Fone from tbclientes where nomecliente like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNomeCli.getText() + "%");
            rs = pst.executeQuery();
            tblCliOs.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void setar_campos() {
        int setar = tblCliOs.getSelectedRow();
        txtIdCli.setText(tblCliOs.getModel().getValueAt(setar, 0).toString());
        //btnAlterar.setEnabled(true);
        //btnExcluir.setEnabled(true);
        // btnAdicionar.setEnabled(false);
    }

    private void emitir_os() {
        String sql = "insert into tbos (tipo,situacao,equipamento,defeito,servico,tecnico,datadevolucao,valor,idcliente) values (?,?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, cboSituacao.getSelectedItem().toString());
            pst.setString(3, txtEquipOs.getText());
            pst.setString(4, txtDefOs.getText());
            pst.setString(5, txtServOs.getText());
            pst.setString(6, txtTecOs.getText());
            pst.setString(7, txtOsDtDev.getText());
            pst.setString(8, txtVlrOs.getText().replace(",", "."));
            pst.setString(9, txtIdCli.getText());

            if ((txtIdCli.getText().isEmpty() || txtEquipOs.getText().isEmpty() || txtDefOs.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios.");
            } else {
                int retorno = pst.executeUpdate();
                if (retorno > 0) {
                    JOptionPane.showMessageDialog(null, "Os emitida com sucesso.");
                    txtIdCli.setText(null);
                    txtEquipOs.setText(null);
                    txtDefOs.setText(null);
                    txtServOs.setText(null);
                    txtTecOs.setText(null);
                    txtOsDtDev.setText(null);
                    txtVlrOs.setText("0");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void pesquisar() {
        String num_os = JOptionPane.showInputDialog("Numero da OS:");
        String sql = "select * from tbos where idos= " + num_os;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtOsId.setText(rs.getString(1));
                txtOsData.setText(rs.getString(2));
                String radio = rs.getString(3);
                if (radio.equals("Orçamento")) {
                    rdbOrcamento.setSelected(true);
                    tipo = "Orçamento";
                } else {
                    rdbOrdemServ.setSelected(true);
                    tipo = "OS";
                }
                cboSituacao.setSelectedItem(rs.getString(4));
                txtEquipOs.setText(rs.getString(5));
                txtDefOs.setText(rs.getString(6));
                txtServOs.setText(rs.getString(7));
                txtTecOs.setText(rs.getString(8));
                txtOsDtDev.setText(rs.getString(9));
                txtVlrOs.setText(rs.getString(10));
                txtIdCli.setText(rs.getString(11));
                String aux = txtOsId.getText();
                String sql2 = "select * from tbrecebiveisos where idos=" + aux;

                rs = pst.executeQuery(sql2);
                if (rs.next() || radio.equals("Orçamento")) {
                    btnNota.setEnabled(false);
                } else {
                    btnNota.setEnabled(true);
                }
                btnExcluirOs.setEnabled(true);
                btnAlterarOs.setEnabled(true);
                btnImprimirOs.setEnabled(true);
                btnAdicionarOs.setEnabled(false);
                txtNomeCli.setEnabled(false);
                tblCliOs.setVisible(false);
                txtIdCli.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "OS não encontrada.");
                txtOsId.setText(null);
                txtOsData.setText(null);
                txtIdCli.setText(null);
                txtEquipOs.setText(null);
                txtDefOs.setText(null);
                txtServOs.setText(null);
                txtTecOs.setText(null);
                txtOsDtDev.setText(null);
                txtVlrOs.setText("0");
                btnNota.setEnabled(false);
                btnExcluirOs.setEnabled(false);
                btnAlterarOs.setEnabled(false);
                btnImprimirOs.setEnabled(false);
                btnAdicionarOs.setEnabled(true);
                txtNomeCli.setEnabled(true);
                tblCliOs.setVisible(true);
                txtIdCli.setEnabled(true);

            }

        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e) {
            JOptionPane.showMessageDialog(null, "OS Invalida");
            txtOsId.setText(null);
            txtOsData.setText(null);
            txtIdCli.setText(null);
            txtEquipOs.setText(null);
            txtDefOs.setText(null);
            txtServOs.setText(null);
            txtTecOs.setText(null);
            txtOsDtDev.setText(null);
            txtVlrOs.setText("0");
            btnNota.setEnabled(false);
            btnExcluirOs.setEnabled(false);
            btnAlterarOs.setEnabled(false);
            btnImprimirOs.setEnabled(false);
            btnAdicionarOs.setEnabled(true);
            txtNomeCli.setEnabled(true);
            tblCliOs.setVisible(true);
            txtIdCli.setEnabled(true);
            //System.out.println(e);
        } catch (Exception x) {
            JOptionPane.showMessageDialog(null, x);
        }
    }

    private void alterar_os() {
        String sql = "update tbos set tipo=?,situacao=?,equipamento=?,defeito=?,servico=?,tecnico=?,datadevolucao=?,valor=? where idos=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, cboSituacao.getSelectedItem().toString());
            pst.setString(3, txtEquipOs.getText());
            pst.setString(4, txtDefOs.getText());
            pst.setString(5, txtServOs.getText());
            pst.setString(6, txtTecOs.getText());
            pst.setString(7, txtOsDtDev.getText());
            pst.setString(8, txtVlrOs.getText().replace(",", "."));
            pst.setString(9, txtOsId.getText());

            if ((txtEquipOs.getText().isEmpty() || txtDefOs.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios.");
            } else {
                int retorno = pst.executeUpdate();
                if (retorno > 0) {
                    JOptionPane.showMessageDialog(null, "OS atualizada com sucesso.");
                    txtOsId.setText(null);
                    txtOsData.setText(null);
                    txtIdCli.setText(null);
                    txtEquipOs.setText(null);
                    txtDefOs.setText(null);
                    txtServOs.setText(null);
                    txtTecOs.setText(null);
                    txtOsDtDev.setText(null);
                    txtVlrOs.setText("0");
                    btnNota.setEnabled(false);
                    btnExcluirOs.setEnabled(false);
                    btnAlterarOs.setEnabled(false);
                    btnImprimirOs.setEnabled(false);
                    btnAdicionarOs.setEnabled(true);
                    txtNomeCli.setEnabled(true);
                    tblCliOs.setVisible(true);
                    txtIdCli.setEnabled(true);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void excluir_os() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir OS", "Atençao!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbos where idos=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtOsId.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "OS excluida com sucesso!");
                    txtOsId.setText(null);
                    txtOsData.setText(null);
                    txtIdCli.setText(null);
                    txtEquipOs.setText(null);
                    txtDefOs.setText(null);
                    txtServOs.setText(null);
                    txtTecOs.setText(null);
                    txtOsDtDev.setText(null);
                    txtVlrOs.setText("0");
                    btnNota.setEnabled(false);
                    btnExcluirOs.setEnabled(false);
                    btnAlterarOs.setEnabled(false);
                    btnImprimirOs.setEnabled(false);
                    btnAdicionarOs.setEnabled(true);
                    txtNomeCli.setEnabled(true);
                    tblCliOs.setVisible(true);
                    txtIdCli.setEnabled(true);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }

    private void imprimir_os() {
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão da OS?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                HashMap filtro = new HashMap();
                filtro.put("os", Integer.parseInt(txtOsId.getText()));
                JasperPrint jp = JasperFillManager.fillReport("C:/Users/m3tech/Documents/m3tech_os/m3techOs/relatorios/Os.jasper", filtro, conexao);
                JasperViewer.viewReport(jp, false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void recebimento_os() {
        int confirma = JOptionPane.showConfirmDialog(null, "Gerar fatura para essa OS?", "Atençao!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "insert into tbrecebiveisos (idos,statusdoc) values (?,?)";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtOsId.getText());
                pst.setString(2, "Aberta");
                pst.executeUpdate();
                btnNota.setEnabled(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtOsId = new javax.swing.JTextField();
        txtOsData = new javax.swing.JTextField();
        rdbOrcamento = new javax.swing.JRadioButton();
        rdbOrdemServ = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        cboSituacao = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        txtNomeCli = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtIdCli = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCliOs = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtEquipOs = new javax.swing.JTextField();
        txtDefOs = new javax.swing.JTextField();
        txtServOs = new javax.swing.JTextField();
        txtTecOs = new javax.swing.JTextField();
        txtVlrOs = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtOsDtDev = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        btnAdicionarOs = new javax.swing.JButton();
        btnPesquisarOs = new javax.swing.JButton();
        btnAlterarOs = new javax.swing.JButton();
        btnExcluirOs = new javax.swing.JButton();
        btnImprimirOs = new javax.swing.JButton();
        btnNota = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();

        jLabel4.setText("jLabel4");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cadastro de OS");
        setPreferredSize(new java.awt.Dimension(596, 518));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel1.setText("Nº OS");

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel2.setText("Data");

        txtOsId.setEditable(false);

        txtOsData.setEditable(false);

        buttonGroup1.add(rdbOrcamento);
        rdbOrcamento.setText("Orçamento");
        rdbOrcamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbOrcamentoActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdbOrdemServ);
        rdbOrdemServ.setText("Ordem de Serviço");
        rdbOrdemServ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbOrdemServActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rdbOrcamento)
                        .addGap(18, 18, 18)
                        .addComponent(rdbOrdemServ))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txtOsId, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtOsData))))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOsId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOsData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdbOrcamento)
                    .addComponent(rdbOrdemServ))
                .addContainerGap())
        );

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel3.setText("Situação");

        cboSituacao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Na bancada", "Entrega OK", "Orçamento REPROVADO", "Aguardando Aprovação", "Aguardando peças", "Abandonado pelo cliente", "Retornou na garantia", "Retornou fora da garantia" }));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Verdana", 1, 12))); // NOI18N

        txtNomeCli.setToolTipText("Digite o nome do cliente.");
        txtNomeCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNomeCliKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNomeCliKeyReleased(evt);
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/m3techos/icones/search.png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel6.setText("ID*");

        txtIdCli.setEditable(false);

        tblCliOs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Nome", "Fone"
            }
        ));
        tblCliOs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCliOsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCliOs);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtNomeCli)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(txtIdCli, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtNomeCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel12.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel12.setText("Equipamento*");

        jLabel13.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel13.setText("Defeito*");

        jLabel14.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel14.setText("Serviço");

        jLabel15.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel15.setText("Técnico");

        jLabel16.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel16.setText("Valor Total");

        txtVlrOs.setText("0");

        jLabel7.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel7.setText("Data devolução");

        try {
            txtOsDtDev.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        btnAdicionarOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/m3techos/icones/add.png"))); // NOI18N
        btnAdicionarOs.setToolTipText("Adcionar");
        btnAdicionarOs.setPreferredSize(new java.awt.Dimension(80, 80));
        btnAdicionarOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarOsActionPerformed(evt);
            }
        });

        btnPesquisarOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/m3techos/icones/read.png"))); // NOI18N
        btnPesquisarOs.setToolTipText("Localizar");
        btnPesquisarOs.setPreferredSize(new java.awt.Dimension(80, 80));
        btnPesquisarOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarOsActionPerformed(evt);
            }
        });

        btnAlterarOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/m3techos/icones/update.png"))); // NOI18N
        btnAlterarOs.setToolTipText("Editar");
        btnAlterarOs.setEnabled(false);
        btnAlterarOs.setPreferredSize(new java.awt.Dimension(80, 80));
        btnAlterarOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarOsActionPerformed(evt);
            }
        });

        btnExcluirOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/m3techos/icones/delete.png"))); // NOI18N
        btnExcluirOs.setToolTipText("Excluir");
        btnExcluirOs.setEnabled(false);
        btnExcluirOs.setPreferredSize(new java.awt.Dimension(80, 80));
        btnExcluirOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirOsActionPerformed(evt);
            }
        });

        btnImprimirOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/m3techos/icones/print.png"))); // NOI18N
        btnImprimirOs.setToolTipText("Imprimir OS");
        btnImprimirOs.setEnabled(false);
        btnImprimirOs.setPreferredSize(new java.awt.Dimension(80, 80));
        btnImprimirOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirOsActionPerformed(evt);
            }
        });

        btnNota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/m3techos/icones/recebimento.png"))); // NOI18N
        btnNota.setToolTipText("Gerar recebimento.");
        btnNota.setEnabled(false);
        btnNota.setPreferredSize(new java.awt.Dimension(80, 80));
        btnNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(btnAdicionarOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnPesquisarOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAlterarOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnExcluirOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnImprimirOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImprimirOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExcluirOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAlterarOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesquisarOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdicionarOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel8.setText("Os campos * são obrigatórios.");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEquipOs)
                            .addComponent(txtDefOs)
                            .addComponent(txtServOs)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(txtTecOs, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(txtOsDtDev, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel16)
                                .addGap(18, 18, 18)
                                .addComponent(txtVlrOs, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(84, 84, 84))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel8)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtEquipOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtDefOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtServOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtTecOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(txtVlrOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtOsDtDev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(29, 29, 29)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(140, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(cboSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cboSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );

        setBounds(0, 0, 840, 695);
    }// </editor-fold>//GEN-END:initComponents

    private void txtNomeCliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeCliKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeCliKeyPressed

    private void txtNomeCliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeCliKeyReleased
        // pesquisar cliente
        pesquisar_cliente();
    }//GEN-LAST:event_txtNomeCliKeyReleased

    private void tblCliOsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCliOsMouseClicked
        // selecionar o cliente
        setar_campos();
    }//GEN-LAST:event_tblCliOsMouseClicked

    private void rdbOrcamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbOrcamentoActionPerformed
        // TODO add your handling code here:
        tipo = "Orçamento";
    }//GEN-LAST:event_rdbOrcamentoActionPerformed

    private void rdbOrdemServActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbOrdemServActionPerformed
        // TODO add your handling code here:
        tipo = "OS";
    }//GEN-LAST:event_rdbOrdemServActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // setar o radio orçamento
        rdbOrcamento.setSelected(true);
        tipo = "Orçamento";
    }//GEN-LAST:event_formInternalFrameOpened

    private void btnAdicionarOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarOsActionPerformed
        // cadastra os
        emitir_os();
    }//GEN-LAST:event_btnAdicionarOsActionPerformed

    private void btnPesquisarOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarOsActionPerformed
        // pesquisar a os
        pesquisar();
    }//GEN-LAST:event_btnPesquisarOsActionPerformed

    private void btnAlterarOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarOsActionPerformed
        // edita os
        alterar_os();
    }//GEN-LAST:event_btnAlterarOsActionPerformed

    private void btnExcluirOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirOsActionPerformed
        // excluir os
        excluir_os();
    }//GEN-LAST:event_btnExcluirOsActionPerformed

    private void btnImprimirOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirOsActionPerformed
        imprimir_os();
    }//GEN-LAST:event_btnImprimirOsActionPerformed

    private void btnNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotaActionPerformed
        // gerar recebimento da os
        recebimento_os();
    }//GEN-LAST:event_btnNotaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionarOs;
    private javax.swing.JButton btnAlterarOs;
    private javax.swing.JButton btnExcluirOs;
    private javax.swing.JButton btnImprimirOs;
    private javax.swing.JButton btnNota;
    private javax.swing.JButton btnPesquisarOs;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cboSituacao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdbOrcamento;
    private javax.swing.JRadioButton rdbOrdemServ;
    private javax.swing.JTable tblCliOs;
    private javax.swing.JTextField txtDefOs;
    private javax.swing.JTextField txtEquipOs;
    private javax.swing.JTextField txtIdCli;
    private javax.swing.JTextField txtNomeCli;
    private javax.swing.JTextField txtOsData;
    private javax.swing.JFormattedTextField txtOsDtDev;
    private javax.swing.JTextField txtOsId;
    private javax.swing.JTextField txtServOs;
    private javax.swing.JTextField txtTecOs;
    private javax.swing.JTextField txtVlrOs;
    // End of variables declaration//GEN-END:variables
}
