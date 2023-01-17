import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

    /**
     * @author June
     *
     *
     */
    public class EditWindow extends JFrame {

        // Class scope "fields" used in different methods of this class
        // and therefore are declared here.
        // Always make variables with as little scope as possible
        private JPanel contentPane;
        private JTree myTree;
        private static boolean DEBUG = true;


        private FileInfo selectFile = null;

        /**
         * Launch the application.
         */

        /**
         * Create the frame.
         */
        public void RCM(Path root) {

            // Called when a node of the tree is selected
            myTree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    // Get the selected tree node (either file or folder)
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) myTree.getLastSelectedPathComponent();

                    if (node == null)
                        return;

                    Object nodeInfo = node.getUserObject();

                    // cast the node's Object to "FileInfo" to get data
                    // This cast raises an exception if the top-most-node is selected....why?
                    FileInfo info = (FileInfo) nodeInfo;
                    System.out.println("Selected info=" + info.fullFilename + "  Dir: " + info.isDirectory);

                    // Currently selected
                    selectFile = info;

                }
            });
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

            public String toString() {
                return filename;
            }
        }
    }


