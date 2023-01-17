import javax.imageio.metadata.IIOMetadataNode;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class RCM {
    private JPanel main;
    private JTree tree1;
    private JButton addDir;
    private JButton delDir;
    private JButton addCom;
    private JButton editCom;
    private JButton openCom;
    private JButton delCom;
    private JTextArea textArea1;
    private JButton save;
    private JButton cancel;
    private JTextField textArea2;
    private JButton load;
    private JTextField textArea3;
    private static RCM frame;
    public boolean isDirectory;

    private DefaultMutableTreeNode root;
    private FileInfo selectFile;
    String filename;
    String path;
    private boolean visible;

    public RCM() {

        treeRoot();

        // A set of button
        addDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser txt = new JFileChooser();
                txt.showOpenDialog(null);
                File f = txt.getSelectedFile();
                String filename = f.getAbsolutePath();

            }
        });

        delDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DelDir();

            }
        });

        addCom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AddComment();
            }
        });

        openCom.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

//               String path = null;
                JFileChooser txt = new JFileChooser("");
                txt.showOpenDialog(null);
                File f = txt.getSelectedFile();

               String filename = f.getAbsolutePath();
               textArea2.setText(filename);
            }
        });

        delCom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletefile ();

            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                saveFile();
        }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelfile();

            }
        });

        tree1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                IIOMetadataNode node = new IIOMetadataNode();
                Object nodeInfo = node.getUserObject();
                FileInfo info = (FileInfo) nodeInfo;
                super.mouseClicked(e);


            }
        });

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    loadFile();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();

                }
            }
        });

    }

    /////////////////////////////////////Method////////////////////////////////

    private void treeRoot() {
        Path topDir;

        topDir = FileSystems.getDefault().getPath(System.getProperty("user.dir"));

        // Create the nodes.
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(topDir);
        // Start the recursive creation of the tree
        createNodes(top, topDir);


        DefaultTreeModel treeModel =(DefaultTreeModel)tree1.getModel();
        treeModel.setRoot(top);

        tree1.addTreeSelectionListener(new TreeSelectionListener() {
            private FileInfo selectFile = null;

            public void valueChanged(TreeSelectionEvent e) {
                // Get the selected tree node (either file or folder)
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();

                if (node == null)
                    return;

                Object nodeInfo = node.getUserObject();

                FileInfo info = (FileInfo) nodeInfo;
                System.out.println("Selected info=" + info.fullFilename + "  Dir: " + info.isDirectory);

                // Currently selected
                selectFile = info;
                //Catch the file name and display inside the TextArea3
                String filename = selectFile.fullFilename;
                textArea3.setText(filename);
            }
        });
    }


    private void createNodes(DefaultMutableTreeNode nextTop, Path nextDir) {

        // Use java.nio.file.DirectoryStream library
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(nextDir)) {
            for (Path file : stream) {
                FileInfo fileInfo = new FileInfo(file);
                DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(fileInfo);
                if (fileInfo.isDirectory) {
                    nextTop.add(aNode);
                    createNodes(aNode, file); // Recursively build the tree...a new folder here
                } else if (Files.isRegularFile(file)) {
                    nextTop.add(aNode);
                }
            }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            System.err.println(x);
        }
    }

    private void rcmTemplate() {
        File template1 = new File("");
        Scanner readtemplate = new Scanner("rcmtemplate.txt");
        try {
            FileWriter writetemplate = new FileWriter(template1);
            PrintWriter printtemplate = new PrintWriter(writetemplate);

            printtemplate.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void AddComment() {

        Runtime rc = Runtime.getRuntime();

        try {
            rc.exec("notepad");
        } catch (IOException e) {
            System.out.print(e);
        }

    }

    private void OpenFileTemplate(File aFile) {

        // set up file path
        final String path;

        // Get file path and name
        if (aFile != null) {
            // get path and name from file
            path = aFile.getAbsolutePath();
        } else {
            // default path and name
            path = "";
        }
    }

    private void saveFile() {
//        String path = "";
        File newFile = new File(path,String.valueOf(textArea2.getText()));
        try {
            FileWriter fw1 = new FileWriter(newFile);
            PrintWriter pw1 = new PrintWriter(fw1);
            pw1.write(String.valueOf(textArea1.getText()));
            // saving the data
            pw1.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        writeFile(String.valueOf(newFile), filename, textArea1.getText());

    }

    private void loadFile() throws FileNotFoundException {
        File clickfile = new File(String.valueOf(textArea2.getText()));
        StringBuffer content = new StringBuffer();

        Scanner fileLoader;
        try {
            fileLoader = new Scanner(clickfile);
            while (fileLoader.hasNextLine()) {
                String data = fileLoader.nextLine();
                System.out.println(data);
                content.append(data);
                textArea1.setText(String.valueOf(content));
                content.append("\n");
            }
            fileLoader.close();
        }catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

   private void deletefile () {
        File clickfile = new File(String.valueOf(textArea3.getText()));
//        StringBuffer content = new StringBuffer();

        if (clickfile.delete()) {
            System.out.println("Deleted the file: " + clickfile.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
    }

    private void DelDir() {

        File mouseclickobj = new File(String.valueOf(textArea3.getText()));

        if (mouseclickobj.delete()) {
            System.out.println("Deleted the folder: " + mouseclickobj.getName());
        } else {
            System.out.println("Failed to delete the folder.");
        }

    }

    private void cancelfile () {
        File clickfile = new File(String.valueOf(textArea2.getText()));

        System.out.println("  Cancel: " + clickfile);

        // go back to the orginal load file
        try {

            loadFile();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();

        }
    }


    private String readFile(final String path) {

        System.out.println("  Reading: " + path);

        String content = "";

        try {
            content = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
            content = "File was not found";
        }
        return content;
    }

        public void writeFile(final String newFile, String text, String textArea1Text) {
        PrintWriter fileOut;

        System.out.println("  Saving: " + newFile);

        try {
            fileOut = new PrintWriter(new FileWriter(newFile));
            fileOut.println(textArea1.getText());
            fileOut.close();
        } catch (Exception e) {
            System.err.println("Error of saving file");
        }

    }

   public class FileNode {

        private File file;

        public FileNode(File file) {
            this.file = file;
        }

        @Override
        public String toString() {
            String name = file.getName();
            if (name.equals("")) {
                return file.getAbsolutePath();
            } else {
                return name;
            }
        }
    }

    private class FileInfo {
        public Path file;
        public String filename;
        public String fullFilename;
        public boolean isDirectory;

        public FileInfo(Path file) {
            this.file = file;
            this.filename = file.getName(file.getNameCount() - 1).toString();
            this.fullFilename = file.toString();
            this.isDirectory = Files.isDirectory(file);
        }
        // file name return back to string
        public String toString() {
            return filename;
        }
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("RCM");
        frame.setContentPane(new RCM().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
