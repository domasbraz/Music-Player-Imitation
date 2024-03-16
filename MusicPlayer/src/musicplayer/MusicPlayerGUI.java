/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package musicplayer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
    DefaultTableModel likedPlaylistTableModel;
    DefaultTableModel playlist1TableModel;
    DefaultTableModel playlist2TableModel;
    private JTable currPlaylist;
    private StackInterface likedSongs = new MyStack();
    private StackInterface likedSongsSearch = new MyStack();
    private LinearListInterface dllAllSongs = new DLList();
    private LinearListInterface dllAllSongsSearch = new DLList();
    private LinearListInterface dllPlaylist1 = new DLList();
    private LinearListInterface dllPlaylist1Search = new DLList();
    private LinearListInterface dllPlaylist2 = new DLList();
    private LinearListInterface dllPlaylist2Search = new DLList();
    private ArrayList<JTable> playlists = new ArrayList<>();
    private boolean searchActive = false;
    private boolean deleteMode = false;
    private JScrollPane currContainer;
    private boolean active = true;
    
    
    public MusicPlayerGUI()
    {
        defaultMethod();

        //this button is made for testing purposes
        checkDll.setVisible(false);
        
    }

    public void defaultMethod()
    {
        makeTableModels();
        initComponents();
        applyTableInteractions();
        disableTableEditing();
        //displays background colour
        jLayeredPane1.setOpaque(true);
        hideElements();
        currPlaylist = defaultPlaylist;
        lblPlaylistName.setText("All Songs");
        likePlaylistScrollPane.setVisible(false);
        currContainer = defaultPlaylistScrollPane;
        playlist1ScrollPane.setVisible(false);
        pnlPlaylistPrompt.setVisible(false);
        
    }
    
    public void hideElements()
    {
        pnlMenu.setVisible(false);
        pnlAddSong.setVisible(false);
        btnFinishDelete.setVisible(false);
        lblDeleteHelp.setVisible(false);
    }
    
    public void makeTableModels()
    {
        defaultPlaylistTableModel = new DefaultTableModel(
            new Object [][]
            {},
            new String []
            {
                "Name", "Genre", "Add to Liked", "Move", "Move"
            }
        );
        
        likedPlaylistTableModel = new DefaultTableModel(
                new Object[][] {},
                new String[]
                {
                    "Name","Genre","Add to Playlist"
                }
        );
        
        playlist1TableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]
                {
                    "Name", "Genre", "Remove From Playlist", "Move", "Move"
                }
        );
        
        
    }
    
    public void disableTableEditing()
    {
        for (JTable table : playlists)
        {
            for (int i = 0; i < table.getColumnCount(); i++) 
            {
                TableColumn column = table.getColumnModel().getColumn(i);
                column.setResizable(false);
            }
            table.getTableHeader().setReorderingAllowed(false);
            table.setDefaultEditor(Object.class, null); 
        }
        
//        for (int i = 0; i < defaultPlaylist.getColumnCount(); i++) {
//            TableColumn column = defaultPlaylist.getColumnModel().getColumn(i);
//            column.setResizable(false);
//        }
//        defaultPlaylist.getTableHeader().setReorderingAllowed(false);
//        defaultPlaylist.setDefaultEditor(Object.class, null); 
    }
    
    public void applyTableInteractions()
    {
        for (JTable table : playlists)
        {
            addEventListenerToTable(table);
        }
    }
    
    //ref: https://gist.github.com/nis4273/c01c4e339b557f965797
    public void addEventListenerToTable(JTable table)
    {
        table.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0)
                {
                    tableInteraction(row, col, table);
                }
            }
        });
    }
    
    public void addNewTableRow(String[] data, JTable table)
    {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.addRow(data);
        linkTable(data, table);
        
//        switch (table)
//        {
//            case "default":
//                defaultPlaylistTableModel.addRow(data);
//                linkTable(data);
//                break;
//        }
        
    }
    
    public boolean isSongNameTaken(String name)
    {
        for (int x = dllAllSongs.size(); x > 0; x--)
        {
            String[] data = dllAllSongs.printItem(x).split("%");
            if (data[0].equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;
    }
    
    public void searchName(String name, JTable table)
    {
        if (searchActive)
        {
            updateTable(table, false);
            clearSearch();
            searchName(name, table);
        }
        else
        {
            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        
            int rows = tableModel.getRowCount();

            Pattern pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);

            while (rows > 0)
            {
                rows--;
                Matcher matcher = pattern.matcher(table.getValueAt(rows , 0).toString());
                if (!(matcher.find()))
                {
                    tableModel.removeRow(rows);
                }
            }
            rows = tableModel.getRowCount();
            for (int x = 0; x < rows; x++)
            {
                String data = "";
                data += table.getValueAt(x, 0) + "%";
                data += table.getValueAt(x, 1) + "%";
                data += table.getValueAt(x, 2) ;
                if (currPlaylist != likedPlaylist)
                {
                    whichTableDll(true).add(x + 1, data);
                }
                else
                {
                    String[] dataArray = data.split("%");
                    String[] songInfo = {dataArray[0], dataArray[1]};
                    likedSongsSearch.push(songInfo);
                }
            }
            searchActive = true;
        }

    }
    
    public void clearSearch()
    {
        if (currPlaylist != likedPlaylist)
        {
            whichTableDll(true).removeAll();
        }
        else
        {
            likedSongsSearch.emptyStack();
        }
        searchActive = false;
    }
    

    
    public LinearListInterface whichTableDll(boolean isSearchDll)
    {
        if (currPlaylist == defaultPlaylist)
        {
            return isSearchDll ? dllAllSongsSearch : dllAllSongs;
        }
        if (currPlaylist == playlist1)
        {
            return isSearchDll ? dllPlaylist1Search : dllPlaylist1;
        }
        
        //this statement should never be executed
        return null;
    }
    
    public void updateTable(JTable table, boolean search)
    {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        
        if (!(table == likedPlaylist))
        {
            if (search)
            {
                LinearListInterface dll = whichTableDll(true);
                emptyTable(table);

                for (int x = 0; x < dll.size(); x++)
                {
                    String[] song = dll.printItem(x + 1).split("%");

                    String[] data = getSongInfo(table, song);
                    tableModel.insertRow(x, data);
                }
            }
            else
            {
                LinearListInterface dll = whichTableDll(false);

                emptyTable(table);

                for (int x = 0; x < dll.size(); x++)
                {
                    String[] song = dll.printItem(x + 1).split("%");

                    String[] data = getSongInfo(table, song);
                    tableModel.insertRow(x, data);

                }
            }
        }
        else
        {
            if (search)
            {
                emptyTable(table);

                for (int x = 0; x < likedSongsSearch.size(); x++)
                {
                    String[] song = likedSongsSearch.getValue(x);

                    String[] data = {song[0], song[1], "<html><b>[Add]</></>"};
                    tableModel.insertRow(x, data);
                }
            }
            else
            {
               
                emptyTable(table);

                for (int x = 0; x < likedSongs.size(); x++)
                {
                    String[] song = likedSongs.getValue(x);

                    String[] data = {song[0], song[1], "<html><b>[Add]</></>"};
                    tableModel.insertRow(x, data);

                }
            }
        }

    }
    
    public String[] getSongInfo(JTable table, String[] song)
    {
        
        if (table == defaultPlaylist)
        {
            return new String[] {song[0], song[1], song[2], "<html><b>[Up]</b></html>", "<html><b>[Down]</b></html>"};
        }
        
        
        
        return null;
    }
    
    public void emptyTable(JTable table)
    {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        
        for (int x = table.getRowCount(); x > 0; x--)
        {
            tableModel.removeRow(x - 1);
        }
    }
    
    public void linkTable(String[] data, JTable table)
    {
        int songCount = table.getRowCount();
        String item = data[0] +  "%" + data[1] + "%" + data[2];
        whichTableDll(false).add(songCount, item);
    }
    
    public String getValueAtRow(int row, JTable table)
    {
        String data = table.getValueAt(row, 0).toString() + "%";
        data += table.getValueAt(row, 1).toString() + "%";
        data += table.getValueAt(row, 2).toString();
        return data;
    }
    
    public void tableInteraction(int row, int col, JTable table)
    {
        if (active)
        {
            if (!deleteMode)
            {
                String value = table.getValueAt(row, col).toString();

                if (value.equalsIgnoreCase("<html><b>[Like]</b></html>") || value.equalsIgnoreCase("<html><b>[Liked]</b></html>"))
                {
                    likeSongOperation(value, row, col, table);
                }

                else if (value.equalsIgnoreCase("<html><b>[Up]</b></html>"))
                {
                    String name = getValueAtRow(row, table);
                    moveSongUp(name);
                    updateTable(table, searchActive);
                }
                else if (value.equalsIgnoreCase("<html><b>[Down]</b></html>"))
                {
                    String name = getValueAtRow(row, table);
                    moveSongDown(name);
                    updateTable(table, searchActive);
                }
                else if (value.equalsIgnoreCase("<html><b>[Add]</></>"))
                {
                    pnlPlaylistPrompt.setVisible(true);
                    active = false;
                }
            }
            else
            {
                deleteSong(row);
            }
        }
        
    }
    
    public void likeSongOperation(String value, int row, int col, JTable table)
    {
        if (value.equalsIgnoreCase("<html><b>[Like]</b></html>"))
        {
            likeSong(row);
            String oldData = getValueAtRow(row, table);
            table.setValueAt("<html><b>[Liked]</b></html>", row, col);
            String newData = getValueAtRow(row, table);
            whichTableDll(false).replace(oldData, newData);
        }
        else if (value.equalsIgnoreCase("<html><b>[Liked]</b></html>"))
        {
            String[] name = new String[2];
            name[0] = table.getValueAt(row, 0).toString();
            name[1] = table.getValueAt(row, 1).toString();
            removeFromLiked(name);
            String oldData = getValueAtRow(row, table);
            table.setValueAt("<html><b>[Like]</b></html>", row, col);
            String newData = getValueAtRow(row, table);
            whichTableDll(false).replace(oldData, newData);
            
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
        likedSongs.push(data);
    }
    
    public void removeFromLiked(String[] name)
    {
        likedSongs.remove(name);
    }
    
    public void moveSongUp(String name)
    {
        whichTableDll(false).sendBackward(name);
        
        if (searchActive)
        {
            whichTableDll(true).sendBackward(name);
        }
        
    }
    
    public void moveSongDown(String name)
    {
        whichTableDll(false).sendForward(name);
        
        if (searchActive)
        {
            whichTableDll(true).sendForward(name);
        }
    }
    
    public boolean doesNameHaveInvalidChar(String name)
    {
        Pattern pattern = Pattern.compile("%");
        Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }
    
    public boolean hasSongs(JTable table)
    {
        return (table.getRowCount() > 0);
    }
    
    public void deleteSong(int row)
    {
        String name = getValueAtRow(row, currPlaylist);
        
        if (currPlaylist == likedPlaylist)
        {
            String[] nameArray = name.split("%");
            nameArray[2] = "%" + "<html><b>[Liked]</b></html>";
            name = nameArray[0] + "%" + nameArray[1] + nameArray[2];
        }
        
        
        dllAllSongs.remove(dllAllSongs.getIndex(name));
        dllAllSongsSearch.remove(dllAllSongsSearch.getIndex(name));
        dllPlaylist1.remove(dllPlaylist1.getIndex(name));
        dllPlaylist2.remove(dllPlaylist2.getIndex(name));

        String[] data = new String[2];
        data[0] = name.split("%")[0];
        data[1] = name.split("%")[1];
        likedSongs.remove(data);
        
        
        updateTable(currPlaylist, searchActive);
    }
    
    public void switchTable(JTable table, JScrollPane container)
    {
        pnlMenu.setVisible(false);
        clearSearch();
        currContainer.setVisible(false);
        container.setVisible(true);
        currContainer = container;
        currPlaylist = table;
        updatePlaylistLabel();
        updateTable(currPlaylist, searchActive);
    }
    
    public void updatePlaylistLabel()
    {
        if (currPlaylist == defaultPlaylist)
        {
            lblPlaylistName.setText("All Songs");
        }
        else if (currPlaylist == likedPlaylist)
        {
            lblPlaylistName.setText("Liked Songs");
        }
        else if (currPlaylist == playlist1)
        {
            lblPlaylistName.setText("Playlist 1");
        }
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
        defaultPlaylistScrollPane = new javax.swing.JScrollPane();
        defaultPlaylist = new javax.swing.JTable();
        btnMenu = new javax.swing.JButton();
        lblPlaylistName = new javax.swing.JLabel();
        pnlAddSong = new javax.swing.JPanel();
        lblAddSong = new javax.swing.JLabel();
        tfSongName = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        cbbGenre = new javax.swing.JComboBox<>();
        lblGenre = new javax.swing.JLabel();
        btnFinalAdd = new javax.swing.JButton();
        btnFinalCancel = new javax.swing.JButton();
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
        checkDll = new javax.swing.JButton();
        lblDeleteHelp = new javax.swing.JLabel();
        btnFinishDelete = new javax.swing.JButton();
        likePlaylistScrollPane = new javax.swing.JScrollPane();
        likedPlaylist = new javax.swing.JTable();
        playlist1ScrollPane = new javax.swing.JScrollPane();
        playlist1 = new javax.swing.JTable();
        pnlPlaylistPrompt = new javax.swing.JPanel();
        btnAddToPlaylist1 = new javax.swing.JButton();
        btnAddToPlaylist2 = new javax.swing.JButton();

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
        playlists.add(defaultPlaylist);
        defaultPlaylistScrollPane.setViewportView(defaultPlaylist);

        btnMenu.setText("Menu");
        btnMenu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMenuActionPerformed(evt);
            }
        });

        lblPlaylistName.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblPlaylistName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPlaylistName.setText("PlaylistName");

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

        checkDll.setText("Check dll");
        checkDll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                checkDllActionPerformed(evt);
            }
        });

        lblDeleteHelp.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblDeleteHelp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDeleteHelp.setText("Select A Song to Delete");

        btnFinishDelete.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnFinishDelete.setText("Done");
        btnFinishDelete.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFinishDeleteActionPerformed(evt);
            }
        });

        likedPlaylist.setBackground(new java.awt.Color(15, 42, 61));
        likedPlaylist.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        likedPlaylist.setForeground(new java.awt.Color(255, 255, 255));
        likedPlaylist.setModel(likedPlaylistTableModel);
        likedPlaylist.setRowHeight(40);
        playlists.add(likedPlaylist);
        likePlaylistScrollPane.setViewportView(likedPlaylist);

        playlist1.setBackground(new java.awt.Color(15, 42, 61));
        playlist1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        playlist1.setModel(playlist1TableModel);
        playlist1.setRowHeight(40);
        playlists.add(playlist1);
        playlist1ScrollPane.setViewportView(playlist1);

        pnlPlaylistPrompt.setBackground(new java.awt.Color(43, 67, 83));

        btnAddToPlaylist1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnAddToPlaylist1.setText("Add To Playlist 1");

        btnAddToPlaylist2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnAddToPlaylist2.setText("Add To Playlist 2");

        javax.swing.GroupLayout pnlPlaylistPromptLayout = new javax.swing.GroupLayout(pnlPlaylistPrompt);
        pnlPlaylistPrompt.setLayout(pnlPlaylistPromptLayout);
        pnlPlaylistPromptLayout.setHorizontalGroup(
            pnlPlaylistPromptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPlaylistPromptLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(pnlPlaylistPromptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAddToPlaylist1)
                    .addComponent(btnAddToPlaylist2))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        pnlPlaylistPromptLayout.setVerticalGroup(
            pnlPlaylistPromptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPlaylistPromptLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(btnAddToPlaylist1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(btnAddToPlaylist2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        jLayeredPane1.setLayer(defaultPlaylistScrollPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(btnMenu, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lblPlaylistName, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(pnlAddSong, javax.swing.JLayeredPane.POPUP_LAYER);
        jLayeredPane1.setLayer(pnlMenu, javax.swing.JLayeredPane.PALETTE_LAYER);
        jLayeredPane1.setLayer(pnlPlaySection, javax.swing.JLayeredPane.MODAL_LAYER);
        jLayeredPane1.setLayer(checkDll, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lblDeleteHelp, javax.swing.JLayeredPane.DRAG_LAYER);
        jLayeredPane1.setLayer(btnFinishDelete, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(likePlaylistScrollPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(playlist1ScrollPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(pnlPlaylistPrompt, javax.swing.JLayeredPane.MODAL_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap(172, Short.MAX_VALUE)
                .addComponent(defaultPlaylistScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113))
            .addComponent(pnlPlaySection, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblPlaylistName, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(btnFinishDelete)
                .addGap(33, 33, 33)
                .addComponent(checkDll)
                .addGap(30, 30, 30))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addComponent(pnlMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 582, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap(244, Short.MAX_VALUE)
                    .addComponent(pnlAddSong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(238, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(187, 187, 187)
                    .addComponent(lblDeleteHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(269, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap(141, Short.MAX_VALUE)
                    .addComponent(likePlaylistScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(116, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap(134, Short.MAX_VALUE)
                    .addComponent(playlist1ScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 552, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(114, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap(256, Short.MAX_VALUE)
                    .addComponent(pnlPlaylistPrompt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(231, Short.MAX_VALUE)))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPlaylistName, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkDll)
                    .addComponent(btnFinishDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(defaultPlaylistScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(pnlPlaySection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlMenu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap(140, Short.MAX_VALUE)
                    .addComponent(pnlAddSong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(184, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(lblDeleteHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(557, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap(85, Short.MAX_VALUE)
                    .addComponent(likePlaylistScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(118, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap(81, Short.MAX_VALUE)
                    .addComponent(playlist1ScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(120, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap(150, Short.MAX_VALUE)
                    .addComponent(pnlPlaylistPrompt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(196, Short.MAX_VALUE)))
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
        btnFinishDeleteActionPerformed(null);
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
        if (currPlaylist != likedPlaylist || searchActive)
        {
            switchTable(likedPlaylist, likePlaylistScrollPane);
        }
        pnlMenu.setVisible(false);
    }//GEN-LAST:event_btnViewLikedActionPerformed

    private void btnViewPlaylist1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnViewPlaylist1ActionPerformed
    {//GEN-HEADEREND:event_btnViewPlaylist1ActionPerformed
        switchTable(playlist1, playlist1ScrollPane);
    }//GEN-LAST:event_btnViewPlaylist1ActionPerformed

    private void btnViewPlaylist2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnViewPlaylist2ActionPerformed
    {//GEN-HEADEREND:event_btnViewPlaylist2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnViewPlaylist2ActionPerformed

    private void btnViewAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnViewAllActionPerformed
    {//GEN-HEADEREND:event_btnViewAllActionPerformed
        if (currPlaylist != defaultPlaylist || searchActive)
        {
            clearSearch();
            switchTable(defaultPlaylist, defaultPlaylistScrollPane);
        }
        pnlMenu.setVisible(false);
    }//GEN-LAST:event_btnViewAllActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSearchActionPerformed
    {//GEN-HEADEREND:event_btnSearchActionPerformed
        pnlMenu.setVisible(false);
        if (hasSongs(currPlaylist))
        {
            String name = JOptionPane.showInputDialog(jLayeredPane1, "Enter song name:");
            if (!(name.isEmpty() || name == null))
            {
                searchName(name, currPlaylist);
            }
            
        }
        else
        {
            JOptionPane.showMessageDialog(jLayeredPane1, "<html><h1>The current playlist has no songs!</></>");
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDeleteActionPerformed
    {//GEN-HEADEREND:event_btnDeleteActionPerformed
        if (hasSongs(currPlaylist))
        {
            lblPlaylistName.setVisible(false);
            lblDeleteHelp.setVisible(true);
            btnFinishDelete.setVisible(true);
            pnlMenu.setVisible(false);
            deleteMode = true;
        }
        else
        {
            JOptionPane.showMessageDialog(jLayeredPane1, "<html><h1>No songs to delete!</></>");
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void cbbGenreActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cbbGenreActionPerformed
    {//GEN-HEADEREND:event_cbbGenreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbGenreActionPerformed

    private void btnFinalAddActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFinalAddActionPerformed
    {//GEN-HEADEREND:event_btnFinalAddActionPerformed
        String songName = tfSongName.getText();
        
        if (doesNameHaveInvalidChar(songName))
        {
            JOptionPane.showMessageDialog(pnlAddSong, "<html><h1>Song name cannot contain '%' character</></>");
        }
        else if (isSongNameTaken(songName))
        {
            JOptionPane.showMessageDialog(pnlAddSong, "<html><h1>Song name is already taken!</h1><br><br><b>Tip:</b> You can include extra information such as artist name, album, etc.<html>");
        }
        else if (songName.isBlank() || songName == null)
        {
            JOptionPane.showMessageDialog(pnlAddSong, "<html><h1>Song name is invalid!</></>");
        }
        else
        {
            switchTable(defaultPlaylist, defaultPlaylistScrollPane);
            String genre = cbbGenre.getSelectedItem().toString();
            
            String[] songInfo = {songName, genre, "<html><b>[Like]</b></html>", "<html><b>[Up]</b></html>", "<html><b>[Down]</b></html>"};
            
            addNewTableRow(songInfo, defaultPlaylist);
            
            pnlAddSong.setVisible(false);
            
            tfSongName.setText("");
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

    private void checkDllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_checkDllActionPerformed
    {//GEN-HEADEREND:event_checkDllActionPerformed
        System.out.println(dllAllSongs.printList());
    }//GEN-LAST:event_checkDllActionPerformed

    private void btnFinishDeleteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFinishDeleteActionPerformed
    {//GEN-HEADEREND:event_btnFinishDeleteActionPerformed
        lblDeleteHelp.setVisible(false);
        lblPlaylistName.setVisible(true);
        btnFinishDelete.setVisible(false);
        deleteMode = false;
    }//GEN-LAST:event_btnFinishDeleteActionPerformed

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
    private javax.swing.JButton btnAddToPlaylist1;
    private javax.swing.JButton btnAddToPlaylist2;
    private javax.swing.JButton btnCloseMenu;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFinalAdd;
    private javax.swing.JButton btnFinalCancel;
    private javax.swing.JButton btnFinishDelete;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnViewAll;
    private javax.swing.JButton btnViewLiked;
    private javax.swing.JButton btnViewPlaylist1;
    private javax.swing.JButton btnViewPlaylist2;
    private javax.swing.JComboBox<String> cbbGenre;
    private javax.swing.JButton checkDll;
    private javax.swing.JTable defaultPlaylist;
    private javax.swing.JScrollPane defaultPlaylistScrollPane;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLabel lblAddSong;
    private javax.swing.JLabel lblDeleteHelp;
    private javax.swing.JLabel lblGenre;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPlaylistName;
    private javax.swing.JScrollPane likePlaylistScrollPane;
    private javax.swing.JTable likedPlaylist;
    private javax.swing.JTable playlist1;
    private javax.swing.JScrollPane playlist1ScrollPane;
    private javax.swing.JPanel pnlAddSong;
    private javax.swing.JPanel pnlMenu;
    private javax.swing.JPanel pnlPlaySection;
    private javax.swing.JPanel pnlPlaylistPrompt;
    private javax.swing.JTextField tfSongName;
    // End of variables declaration//GEN-END:variables
}
