/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package musicplayer;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


/**
 *
 * @author domas
 */
public class MusicPlayerGUI extends javax.swing.JFrame
{

    /**
     * Creates new form MusicPlayerGUI
     */
    
    DefaultTableModel defaultPlaylistTableModel; 
    
    private int defaultPlaylistSongCount = 0;
    private ArrayList<String[]> defaultPlaylistBackup = new ArrayList<>();
    private String currPlaylist = "default";
    private StackInterface stack = new MyStack();
    private LinearListInterface dllAllSongs = new DLList();
    private LinearListInterface dllAllSongsSearch = new DLList();
    
    
    public MusicPlayerGUI()
    {
        makeTableModel();
        
        initComponents();
        
        addEventListenerToTable();
        
        disableTableEditing();
        
        //displays background colour
        jLayeredPane1.setOpaque(true);
        
        hidePanels();

    }
    
    public void hidePanels()
    {
        pnlMenu.setVisible(false);
        pnlAddSong.setVisible(false);
    }
    
    public void makeTableModel()
    {
        defaultPlaylistTableModel = new DefaultTableModel(
            new Object [][]
            {},
            new String []
            {
                "Name", "Genre", "", "Move", "Move"
            }
        );
    }
    
    public void disableTableEditing()
    {
        for (int i = 0; i < defaultPlaylist.getColumnCount(); i++) {
            TableColumn column = defaultPlaylist.getColumnModel().getColumn(i);
            column.setResizable(false);
        }
        defaultPlaylist.getTableHeader().setReorderingAllowed(false);
        defaultPlaylist.setDefaultEditor(Object.class, null); 
    }
    
    //ref: https://gist.github.com/nis4273/c01c4e339b557f965797
    public void addEventListenerToTable()
    {
        defaultPlaylist.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                int row = defaultPlaylist.rowAtPoint(evt.getPoint());
                int col = defaultPlaylist.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0)
                {
                    tableInteraction(row, col);
                }
            }
        });
    }
    
    public void addNewTableRow(String[] data, String table)
    {
        switch (table)
        {
            case "default":
                defaultPlaylistTableModel.addRow(data);
                linkTable(data);
                break;
        }
        
    }
    
    public void searchName(String name, String table)
    {
        switch (table)
        {
            case "default":
                int rows = defaultPlaylistTableModel.getRowCount();
                
                Pattern pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
                
                while (rows > 0)
                {
                    rows--;
                    Matcher matcher = pattern.matcher(defaultPlaylist.getValueAt(rows , 1).toString());
                    if (!(matcher.find()))
                    {
                        defaultPlaylistTableModel.removeRow(rows);
                    }
                }
                rows = defaultPlaylistTableModel.getRowCount();
                for (int x = 0; x < rows; x++)
                {
                    String data = "";
                    data += defaultPlaylist.getValueAt(rows, 0) + " ";
                    data += defaultPlaylist.getValueAt(rows, 1) + " ";
                    data += defaultPlaylist.getValueAt(rows, 3) ;
                    dllAllSongsSearch.add(x + 1, data);
                }
                break;
        }
    }
    
    public void updateTable(String table)
    {
        switch (table)
        {
            case "default":
//                int rows = defaultPlaylistTableModel.getRowCount();
//                
//                while (rows > 0)
//                {
//                    rows--;
//                    defaultPlaylistTableModel.removeRow(rows);
//                    System.out.println("loop 1");
//                }
//                for (int row = 0; row < defaultPlaylistBackup.size(); row++)
//                {
//                    String[] values = defaultPlaylistBackup.get(row);
//                    defaultPlaylistTableModel.addRow(values);
//                }
                
//                    int rows = defaultPlaylistTableModel.getRowCount();
//
//                    while (rows > 0)
//                    {
//                        rows--;
//                        defaultPlaylistTableModel.removeRow(rows);
//                    }
                
                    for (int x = 0; x < dllAllSongs.size(); x++)
                    {
                        String[] song = dllAllSongs.get(x + 1).toString().split(" ");
                        String songName = song[0];
                        
                        
                        if (!(defaultPlaylist.getValueAt(x, 0).toString().equalsIgnoreCase(songName)))
                        {
                            String[] data = {song[0], song[1], song[2], "<html><b>[Up]</b><html>", "<html><b>[Down]</b><html>"};
                            defaultPlaylistTableModel.insertRow(x, data);
                        }
                        else if (!(defaultPlaylist.getValueAt(x, 2).toString().equalsIgnoreCase(song[2])))
                        {
                            defaultPlaylistTableModel.removeRow(x);
                            String[] data = {song[0], song[1], song[2], "<html><b>[Up]</b><html>", "<html><b>[Down]</b><html>"};
                            defaultPlaylistTableModel.insertRow(x, data);
                        }
                        
                    }

                break;
                
            case "defaultSearch":
                
                 for (int x = 0; x < dllAllSongsSearch.size(); x++)
                    {
                        String[] song = dllAllSongsSearch.get(x + 1).toString().split(" ");
                        String songName = song[0];
                        
                        if (!(defaultPlaylist.getValueAt(x, 0).toString().equalsIgnoreCase(songName)))
                        {
                            String[] data = {song[0], song[1], song[2], "<html><b>[Up]</b><html>", "<html><b>[Down]</b><html>"};
                            defaultPlaylistTableModel.insertRow(x, data);
                        }
                        
                    }

                break;
        }
    }
    
    public void linkTable(String[] data)
    {
        String item = data[0] +  " " + data[1] + " " + data[2];
        dllAllSongs.add(defaultPlaylistSongCount, item);
        defaultPlaylistSongCount++;
    }
    
    public String getValueAtRow(int row)
    {
        String data = defaultPlaylist.getValueAt(row, 0).toString() + " ";
        data += defaultPlaylist.getValueAt(row, 1).toString() + " ";
        data += defaultPlaylist.getValueAt(row, 2).toString();
        return data;
    }
    
    public void tableInteraction(int row, int col)
    {
        String value = defaultPlaylist.getValueAt(row, col).toString();
        if (value.equalsIgnoreCase("<html><b>[Like]</b><html>"))
        {
            likeSong(row);
            String oldData = getValueAtRow(row);
            defaultPlaylist.setValueAt("<html><b>[Liked]</b><html>", row, col);
            String newData = getValueAtRow(row);
            dllAllSongs.replace(oldData, newData);
        }
        else if (value.equalsIgnoreCase("<html><b>[Liked]</b><html>"))
        {
            String name = defaultPlaylist.getValueAt(row, 0).toString();
            removeFromLiked(name);
            defaultPlaylist.setValueAt("<html><b>[Like]</b><html>", row, col);
        }
        else if (value.equalsIgnoreCase("[Up]"))
        {
            
        }
        
        
    }
    
    public void likeSong(int row)
    {
        String[] data = new String[2];
        data[0] = defaultPlaylist.getValueAt(row, 0).toString();
        data[1] = defaultPlaylist.getValueAt(row, 1).toString();
        addToLikedPlaylist(data);
    }
    
    public void addToLikedPlaylist(String[] data)
    {
        stack.push(data);
    }
    
    public void removeFromLiked(String name)
    {
        stack.remove(name);
    }
    
    public void moveSongUp(String name)
    {
        dllAllSongs.sendForward(name);
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        defaultPlaylist = new javax.swing.JTable();
        btnMenu = new javax.swing.JButton();
        pnlMenu = new javax.swing.JPanel();
        btnCloseMenu = new javax.swing.JButton();
        btnAddSong = new javax.swing.JButton();
        btnViewLiked = new javax.swing.JButton();
        btnViewPlaylist1 = new javax.swing.JButton();
        btnViewPlaylist2 = new javax.swing.JButton();
        btnViewAll = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        pnlPlaySection = new javax.swing.JPanel();
        pnlAddSong = new javax.swing.JPanel();
        lblAddSong = new javax.swing.JLabel();
        tfSongName = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        cbbGenre = new javax.swing.JComboBox<>();
        lblGenre = new javax.swing.JLabel();
        btnFinalAdd = new javax.swing.JButton();
        btnFinalCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLayeredPane1.setBackground(new java.awt.Color(93, 169, 220));
        jLayeredPane1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                jLayeredPane1KeyPressed(evt);
            }
        });

        defaultPlaylist.setBackground(new java.awt.Color(42, 53, 61));
        defaultPlaylist.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        defaultPlaylist.setForeground(new java.awt.Color(255, 255, 255));
        defaultPlaylist.setModel(defaultPlaylistTableModel);
        defaultPlaylist.setRowHeight(40);
        jScrollPane1.setViewportView(defaultPlaylist);

        btnMenu.setText("Menu");
        btnMenu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenuActionPerformed(evt);
            }
        });

        pnlMenu.setBackground(new java.awt.Color(44, 83, 108));

        btnCloseMenu.setText("Close Menu");
        btnCloseMenu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCloseMenuActionPerformed(evt);
            }
        });

        btnAddSong.setText("Add Song");
        btnAddSong.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAddSongActionPerformed(evt);
            }
        });

        btnViewLiked.setText("View Liked");
        btnViewLiked.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnViewLikedActionPerformed(evt);
            }
        });

        btnViewPlaylist1.setText("View Playlist 1");
        btnViewPlaylist1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnViewPlaylist1ActionPerformed(evt);
            }
        });

        btnViewPlaylist2.setText("View Playlist 2");
        btnViewPlaylist2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnViewPlaylist2ActionPerformed(evt);
            }
        });

        btnViewAll.setText("View All Songs");
        btnViewAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnViewAllActionPerformed(evt);
            }
        });

        btnSearch.setText("Search Song");
        btnSearch.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSearchActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete Song");
        btnDelete.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMenuLayout = new javax.swing.GroupLayout(pnlMenu);
        pnlMenu.setLayout(pnlMenuLayout);
        pnlMenuLayout.setHorizontalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenuLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnViewAll, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnViewPlaylist2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnViewPlaylist1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnViewLiked, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddSong, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCloseMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        pnlMenuLayout.setVerticalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenuLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(btnCloseMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnViewAll, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnViewLiked, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnViewPlaylist1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnViewPlaylist2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAddSong, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(157, Short.MAX_VALUE))
        );

        pnlPlaySection.setBackground(new java.awt.Color(200, 230, 250));

        javax.swing.GroupLayout pnlPlaySectionLayout = new javax.swing.GroupLayout(pnlPlaySection);
        pnlPlaySection.setLayout(pnlPlaySectionLayout);
        pnlPlaySectionLayout.setHorizontalGroup(
            pnlPlaySectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlPlaySectionLayout.setVerticalGroup(
            pnlPlaySectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        pnlAddSong.setBackground(new java.awt.Color(43, 67, 83));

        lblAddSong.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblAddSong.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAddSong.setText("Add Song");

        tfSongName.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        lblName.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblName.setText("Name:");

        cbbGenre.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        cbbGenre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pop", "Rock", "Rap", "Metal", "Country", "Classical", "Other" }));
        cbbGenre.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cbbGenreActionPerformed(evt);
            }
        });

        lblGenre.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblGenre.setText("Genre:");

        btnFinalAdd.setBackground(new java.awt.Color(73, 181, 119));
        btnFinalAdd.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnFinalAdd.setText("Add");
        btnFinalAdd.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFinalAddActionPerformed(evt);
            }
        });

        btnFinalCancel.setBackground(new java.awt.Color(178, 82, 48));
        btnFinalCancel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnFinalCancel.setText("Cancel");
        btnFinalCancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFinalCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAddSongLayout = new javax.swing.GroupLayout(pnlAddSong);
        pnlAddSong.setLayout(pnlAddSongLayout);
        pnlAddSongLayout.setHorizontalGroup(
            pnlAddSongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblAddSong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAddSongLayout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addGroup(pnlAddSongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAddSongLayout.createSequentialGroup()
                        .addGroup(pnlAddSongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblGenre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlAddSongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbbGenre, 0, 130, Short.MAX_VALUE)
                            .addComponent(tfSongName)))
                    .addGroup(pnlAddSongLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(btnFinalAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnFinalCancel)))
                .addGap(41, 41, 41))
        );
        pnlAddSongLayout.setVerticalGroup(
            pnlAddSongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAddSongLayout.createSequentialGroup()
                .addComponent(lblAddSong, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlAddSongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfSongName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblName))
                .addGap(29, 29, 29)
                .addGroup(pnlAddSongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbbGenre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGenre))
                .addGap(27, 27, 27)
                .addGroup(pnlAddSongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFinalCancel)
                    .addComponent(btnFinalAdd))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jLayeredPane1.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(btnMenu, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(pnlMenu, javax.swing.JLayeredPane.PALETTE_LAYER);
        jLayeredPane1.setLayer(pnlPlaySection, javax.swing.JLayeredPane.MODAL_LAYER);
        jLayeredPane1.setLayer(pnlAddSong, javax.swing.JLayeredPane.POPUP_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap(138, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113))
            .addComponent(pnlPlaySection, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(pnlMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 582, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap(244, Short.MAX_VALUE)
                    .addComponent(pnlAddSong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(238, Short.MAX_VALUE)))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(pnlPlaySection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlMenu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap(140, Short.MAX_VALUE)
                    .addComponent(pnlAddSong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(184, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnMenuActionPerformed
    {//GEN-HEADEREND:event_btnMenuActionPerformed
        pnlMenu.setVisible(true);
    }//GEN-LAST:event_btnMenuActionPerformed

    private void btnCloseMenuActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCloseMenuActionPerformed
    {//GEN-HEADEREND:event_btnCloseMenuActionPerformed
        pnlMenu.setVisible(false);
    }//GEN-LAST:event_btnCloseMenuActionPerformed

    private void btnAddSongActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAddSongActionPerformed
    {//GEN-HEADEREND:event_btnAddSongActionPerformed
        // TODO add your handling code here:
        pnlMenu.setVisible(false);
        pnlAddSong.setVisible(true);
    }//GEN-LAST:event_btnAddSongActionPerformed

    private void btnViewLikedActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnViewLikedActionPerformed
    {//GEN-HEADEREND:event_btnViewLikedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnViewLikedActionPerformed

    private void btnViewPlaylist1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnViewPlaylist1ActionPerformed
    {//GEN-HEADEREND:event_btnViewPlaylist1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnViewPlaylist1ActionPerformed

    private void btnViewPlaylist2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnViewPlaylist2ActionPerformed
    {//GEN-HEADEREND:event_btnViewPlaylist2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnViewPlaylist2ActionPerformed

    private void btnViewAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnViewAllActionPerformed
    {//GEN-HEADEREND:event_btnViewAllActionPerformed
        updateTable("default");
        pnlMenu.setVisible(false);
    }//GEN-LAST:event_btnViewAllActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSearchActionPerformed
    {//GEN-HEADEREND:event_btnSearchActionPerformed
        String name = JOptionPane.showInputDialog(jLayeredPane1, "Enter song name:");
        searchName(name, currPlaylist);
        pnlMenu.setVisible(false);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDeleteActionPerformed
    {//GEN-HEADEREND:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void cbbGenreActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cbbGenreActionPerformed
    {//GEN-HEADEREND:event_cbbGenreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbGenreActionPerformed

    private void btnFinalAddActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFinalAddActionPerformed
    {//GEN-HEADEREND:event_btnFinalAddActionPerformed
        String songName = tfSongName.getText();
        
        if (songName.length() > 0)
        {
            
            String genre = cbbGenre.getSelectedItem().toString();
            
            String[] songInfo = {songName, genre, "<html><b>[Like]</b><html>", "<html><b>[Up]</b><html>", "<html><b>[Down]</b><html>"};
            
            addNewTableRow(songInfo, "default");
            
            pnlAddSong.setVisible(false);
            
            tfSongName.setText("");
        }
        else
        {
            JOptionPane.showMessageDialog(pnlAddSong, "Song name is invalid!");
        }
        
    }//GEN-LAST:event_btnFinalAddActionPerformed

    private void btnFinalCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFinalCancelActionPerformed
    {//GEN-HEADEREND:event_btnFinalCancelActionPerformed
        tfSongName.setText("");
        pnlAddSong.setVisible(false);
    }//GEN-LAST:event_btnFinalCancelActionPerformed

    private void jLayeredPane1KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_jLayeredPane1KeyPressed
    {//GEN-HEADEREND:event_jLayeredPane1KeyPressed

    }//GEN-LAST:event_jLayeredPane1KeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(MusicPlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(MusicPlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(MusicPlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(MusicPlayerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new MusicPlayerGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddSong;
    private javax.swing.JButton btnCloseMenu;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFinalAdd;
    private javax.swing.JButton btnFinalCancel;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnViewAll;
    private javax.swing.JButton btnViewLiked;
    private javax.swing.JButton btnViewPlaylist1;
    private javax.swing.JButton btnViewPlaylist2;
    private javax.swing.JComboBox<String> cbbGenre;
    private javax.swing.JTable defaultPlaylist;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAddSong;
    private javax.swing.JLabel lblGenre;
    private javax.swing.JLabel lblName;
    private javax.swing.JPanel pnlAddSong;
    private javax.swing.JPanel pnlMenu;
    private javax.swing.JPanel pnlPlaySection;
    private javax.swing.JTextField tfSongName;
    // End of variables declaration//GEN-END:variables
}
