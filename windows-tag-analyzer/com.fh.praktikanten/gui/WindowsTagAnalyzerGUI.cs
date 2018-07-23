using System;
using System.Collections.Generic;
using System.IO;
using System.Threading;
using System.Windows.Forms;
using System.Drawing;
using WindowsTagAnalyzer.com.fh.praktikanten.io;
using WindowsTagAnalyzer.com.fh.praktikanten.db;
using System.Text.RegularExpressions;

namespace WindowsTagAnalyzer.com.fh.praktikanten.gui
{
    public partial class WindowsTagAnalyzerGUI : Form
    {
        private List<TagReader> allUsedTags = new List<TagReader>();

        TreeNode selectedTreeNode;

        /// <summary>
        /// Thread + Timer for the tag-reading process
        /// </summary>
        private Thread tagReaderThread;
        private System.Windows.Forms.Timer tagReaderTimer;

        /// <summary>
        /// Thread + Timer for the treeView-building process
        /// </summary>
        private Thread treeViewBuilderThread;
        private System.Windows.Forms.Timer treeViewBuilderTimer;

        private Thread markTreeViewThread;

        /// <summary>
        /// This delegate enables asynchronous calls for setting
        /// the text property on a TextBox control.
        /// </summary>
        /// <param name="text"></param>
        delegate void SetTextCallback(string text);

        /// <summary>
        /// This delegate enables asynchronous calls for setting
        /// the TreeNode from the TreeNodeView
        /// </summary>
        /// <param name="text"></param>
        delegate void SetTreeViewCallback(TreeNode rootFolder, TreeNode newNode);

        regexGUI rx = new regexGUI();

        public WindowsTagAnalyzerGUI()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Opens a form to set the reasonable tags for the filetypes
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void SetTags_Click(object sender, EventArgs e)
        {
            SetReasonableTagsGUI a = new SetReasonableTagsGUI();
            a.Visible = true;
        }

        /// <summary>
        /// Reads the selected directory and genereates the treeview
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void ChooseDirectory_Click(object sender, EventArgs e)
        {
            //Don't interrupt my scanning!
            if (this.scanningInProgress())
            {
                return;
            }

            OpenFolder.ShowDialog();

            if ("".Equals(OpenFolder.SelectedPath))
            {
                MessageBox.Show("No Folder Selected!");
                return;
            }
            //Perform all neccessary things to build the treeview
            this.initTreeViewBuilder();

            //Perform all necessary things to scan the hd for tags
            this.initTagReaderProcess();
        }

        /// <summary>
        /// Tests if the worker threads are still running
        /// </summary>
        /// <returns>True when scanning is in progress
        ///          False when scanning is finished
        /// </returns>
        private bool scanningInProgress()
        {
            //TreeView is in building process!
            if (treeViewBuilderThread != null)
            {
                if (treeViewBuilderThread.IsAlive)
                {
                    MessageBox.Show("TreeView building still in progress!!");
                    return true;
                }
            }

            //TreeView is in building process!
            if (tagReaderThread != null)
            {
                if (tagReaderThread.IsAlive)
                {
                    MessageBox.Show("Tag scanning still in progress!!");
                    return true;
                }
            }

            return false;
        }

        /// <summary>
        /// Executes all the init statements for the TreeViewBuilder like:
        /// - register the root node
        /// - start the thread
        /// - start the timer to handle the progress output
        /// </summary>
        private void initTreeViewBuilder()
        {
            //Clear the treeView for another search
            FolderTreeView.Nodes.Clear();

            //The root directory is added first to the tree
            TreeNode rootNode = new TreeNode();
            rootNode.Text = OpenFolder.SelectedPath;
            FolderTreeView.Nodes.Add(rootNode);

            //Let the GUI be responsive!
            this.treeViewBuilderThread = new Thread(delegate() { GetFileList(OpenFolder.SelectedPath, rootNode); });
            this.treeViewBuilderThread.Start();

            //Timer needed for the progress output
            this.treeViewBuilderTimer = new System.Windows.Forms.Timer();
            this.treeViewBuilderTimer.Interval = 500;
            this.treeViewBuilderTimer.Tick += new EventHandler(checkTreeViewReady);
            this.treeViewBuilderTimer.Start();

        }

        /// <summary>
        /// Executes all the init statements for the TagReader like:
        /// - start the thread
        /// - start the timer to handle the progress output
        /// </summary>
        private void initTagReaderProcess()
        {
            //register the thread
            tagReaderThread = new Thread(delegate()
            {
                allUsedTags = TagReader.getAllTagReadersFromDirectory(new DirectoryInfo(OpenFolder.SelectedPath),this);
            });
            //Search asynchronous for TagReaders
            tagReaderThread.Start();

            //Timer needed for the progress output
            this.tagReaderTimer = new System.Windows.Forms.Timer();
            this.tagReaderTimer.Interval = 1000;
            this.tagReaderTimer.Tick += new EventHandler(checkTagReaderReady);
            this.tagReaderTimer.Start();
        }

        /// <summary>
        /// Callback function for the Timer.
        /// Checks if the tree view was successfully built
        /// </summary>
        private void checkTreeViewReady(Object o, EventArgs e)
        {
            if (this.treeViewBuilderThread != null)
            {
                if (!this.treeViewBuilderThread.IsAlive)
                {
                    //Timer is not needed anymore
                    this.treeViewBuilderTimer.Stop();
                    //Tell the user that its over
                    this.SetProgressTreeView("Building tree view... done!!!");
                }
            }
        }

        /// <summary>
        /// Callback function for the Timer.
        /// Checks if the Tags wer successfully read
        /// </summary>
        /// <param name="o"></param>
        /// <param name="e"></param>
        private void checkTagReaderReady(Object o, EventArgs e)
        {
            if (this.tagReaderThread != null)
            {
                if (!this.tagReaderThread.IsAlive)
                {
                    //Timer is not needed anymore
                    this.tagReaderTimer.Stop();
                    //Tell the user that its over
                    this.SetProgressTagReader("Scanning... done!!!");

                    new Thread(delegate() { markSuspiciousTreeNodes(); }).Start();
                }
            }
        }

        /// <summary>
        /// Function to asynchronous set the progressBar
        /// </summary>
        /// <param name="text"></param>
        private void SetProgressTreeView(string text)
        {
            // InvokeRequired required compares the thread ID of the
            // calling thread to the thread ID of the creating thread.
            // If these threads are different, it returns true.
            if (this.progressLabelTreeView.InvokeRequired)
            {
                SetTextCallback d = new SetTextCallback(SetProgressTreeView);
                this.Invoke(d, new object[] { text });
            }
            else
            {
                this.progressLabelTreeView.Text = text;
            }
        }

        /// <summary>
        /// Function to asynchronous set the progressBar for the tagReader
        /// </summary>
        /// <param name="text"></param>
        public void SetProgressTagReader(string text)
        {
            // InvokeRequired required compares the thread ID of the
            // calling thread to the thread ID of the creating thread.
            // If these threads are different, it returns true.
            if (this.progressLabelTreeView.InvokeRequired)
            {
                SetTextCallback d = new SetTextCallback(SetProgressTagReader);
                this.Invoke(d, new object[] { text });
            }
            else
            {
                this.progressLabelTagReader.Text = text;
            }
        }

        /// <summary>
        /// Function to asynchronous register a new TreeNode in the TreeViewer
        /// </summary>
        /// <param name="rootFolder"></param>
        /// <param name="newNode"></param>
        private void SetTreeNode(TreeNode rootFolder, TreeNode newNode)
        {
            // InvokeRequired required compares the thread ID of the
            // calling thread to the thread ID of the creating thread.
            // If these threads are different, it returns true.
            if (this.progressLabelTreeView.InvokeRequired)
            {
                SetTreeViewCallback d = new SetTreeViewCallback(SetTreeNode);
                this.Invoke(d, new object[] { rootFolder, newNode });
            }
            else
            {
                rootFolder.Nodes.Add(newNode);
            }
        }

        /// <summary>
        /// the TreeView is filled with the Folder structer of the chosen RootFolder
        /// </summary>
        /// <param name="Root">The directory to look for the files and subfolders</param>
        /// <param name="RootFolder">The parent TreeNode</param>
        /// <returns></returns>
        public List<string> GetFileList(string Root, TreeNode RootFolder)
        {

            SetProgressTreeView("Building tree view... " + RootFolder.FullPath);

            List<string> FileArray = new List<string>();
            try
            {
                string[] Files = System.IO.Directory.GetFiles(Root);
                string[] Folders = System.IO.Directory.GetDirectories(Root);


                //Adds a node for every folder in the directory to the treeview
                for (int i = 0; i < Folders.Length; i++)
                {
                    TreeNode NewNode = new TreeNode();
                    NewNode.Text = ExtractFileName(Folders[i]);

                    //The new node is added to the treeview
                    //RootFolder.Nodes.Add(NewNode);
                    this.SetTreeNode(RootFolder, NewNode);

                    //The function is recursively called with the parent folder as third parameter
                    FileArray.AddRange(GetFileList(Folders[i], NewNode));
                }

                //Set nodes for every file in the chosen directory
                for (int i = 0; i < Files.Length; i++)
                {
                    TreeNode newFile = new TreeNode();
                    newFile.Text = ExtractFileName(Files[i].ToString());

                    this.SetTreeNode(RootFolder, newFile);
                    //RootFolder.Nodes.Add(newFile);

                    FileArray.Add(Files[i].ToString());

                }
            }
            catch (Exception Ex)
            {
                Ex.ToString();
                //MessageBox.Show(Ex.Message);
            }
            return FileArray;
        }

        /// <summary>
        /// Refreshes the TreeView
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void RefreshButton_Click(object sender, EventArgs e)
        {

            if (this.scanningInProgress())
            {
                return;
            }

            //Clears the treeview
            FolderTreeView.Nodes.Clear();

            if (OpenFolder.SelectedPath != null)
            {
                //The root directory is added first to the tree
                TreeNode rootNode = new TreeNode();
                rootNode.Text = OpenFolder.SelectedPath;
                FolderTreeView.Nodes.Add(rootNode);

                //Refreshes the treeview
                GetFileList(OpenFolder.SelectedPath, rootNode);
            }
        }

        /// <summary>
        /// Opens the detailed view for the chosen file
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void FolderTreeView_NodeMouseDoubleClick(object sender, TreeNodeMouseClickEventArgs e)
        {
            if (this.tagReaderThread != null)
            {
                if (this.tagReaderThread.IsAlive)
                {
                    MessageBox.Show("Scanning still in progress!!");
                    return;
                }
            }

            //Right double click disabled
            if (e.Button == MouseButtons.Right)
            {
                return;
            }

            for (int i = 0; i < allUsedTags.Count; i++)
            {
                //Compares the name of the clicked file with the TagReader files
                if (e.Node.Text == ExtractFileName(allUsedTags[i].getAbsoluteFilePath()))
                {
                    WindowsTagAnalyzerDetails wtag = new WindowsTagAnalyzerDetails(allUsedTags[i], rx);
                }
            }
        }

        /// <summary>
        /// Extracts the file name from the rest of the path
        /// </summary>
        /// <param name="path">The complete path of the file width the filename</param>
        /// <returns>The name of the file without the path</returns>
        private string ExtractFileName(string path)
        {
            string folderName = null;

            for (int i = path.Length - 1; i > 0; i--)
            {
                //read the name backwards till "\"
                folderName = path[i] + folderName;
                if (("" + path[i - 1]) == "\\")
                    return folderName;
            }

            return null;
        }

        /// <summary>
        /// Marks files with suspicious tags in it
        /// </summary>
        private void markSuspiciousTreeNodes()
        {
            

            string filetype;
            //List to store the reasonable tags for the filetype
            List<string> reasonabletags;

            //Dictionary to store the tags that are used by the file
            Dictionary<string, string> usedTags;


            foreach (TagReader tr in allUsedTags)
            {
                //reads all the necessary information
                filetype = ExtractFiletype(tr.getAbsoluteFilePath());

                if (filetype != null)
                {
                    reasonabletags = DAO.getDAO().getReasonableTags(filetype);
                    usedTags = tr.getUsedTags();

                    foreach (KeyValuePair<string, string> kvp in usedTags)
                    {
                        //proofs if one of the tags is not in the reasonable tags list of the filetype
                        if (!reasonabletags.Contains(kvp.Key))
                        {
                            //calls recursive function to mark the suspicious treenodes
                            markTreeNode(FolderTreeView.Nodes[0], ExtractFileName(tr.getAbsoluteFilePath()));
                        }
                      
                        //checking for regex
                        foreach (Object regex in rx.clb.CheckedItems)
                        {
                            if (proofRegex(kvp, regex.ToString()))
                            {
                                //if there is a regex in the tag value the treenode is marked orange
                                markTreeNodeRegex(FolderTreeView.Nodes[0], ExtractFileName(tr.getAbsoluteFilePath()));
                            }
                        }
                    }

                    
                }
            }

            
        }

        /// <summary>
        /// recursively checks the treenodes for the file
        /// marks the treenode with the name of the suspicious file
        /// if the treenode is already orange because of a regex the orange won't be overwritten
        /// </summary>
        /// <param name="currentTreeNode">the current treenode that has to be checked</param>
        /// <param name="fileName">the name of the file with suspicious tags in it, that has t be marked</param>
        private void markTreeNode(TreeNode currentTreeNode, string fileName)
        {
            //if the current tree node is the suspicious file the treenode is marked red
            if (currentTreeNode.Text == fileName)
            {
                //if the treenode is already orange, it won't be colored red
                if (currentTreeNode.BackColor != Color.Orange)
                {
                    currentTreeNode.BackColor = Color.Red;
                }
                return;
            }
            else
            {
                //else the next treenode is checked
                for (int i = 0; i < currentTreeNode.Nodes.Count; i++)
                {
                    markTreeNode(currentTreeNode.Nodes[i], fileName);
                }
            }
        }

        /// <summary>
        /// marks the treenodes with regexes in it orange
        /// recursively searches the treeview for the searched treenode and marks it orange 
        /// </summary>
        /// <param name="currentTreeNode">the treenode that has to be checked</param>
        /// <param name="fileName">the name of the file with a tag value that has a regex in it</param>
        private void markTreeNodeRegex(TreeNode currentTreeNode, string fileName)
        {
            //checks if the current treenode is the searched file
            if (currentTreeNode.Text == fileName)
            {
                currentTreeNode.BackColor = Color.Orange;
                return;
            }
            else
            {
                //else the next treenode is checked
                for (int i = 0; i < currentTreeNode.Nodes.Count; i++)
                {
                    markTreeNodeRegex(currentTreeNode.Nodes[i], fileName);
                }
            }
        }

        /// <summary>
        /// checks the tags with the chosen regexes
        /// </summary>
        /// <param name="kvp">a keyvaluepair with the tag name and value in it which has to be checked</param>
        /// <returns>true if a regex is found and false if no regex is found</returns>
        private Boolean proofRegex(KeyValuePair <string, string> kvp, String regex)
        {
            
                //checks if the URL regex is checked in the regexGUI
            if (regex.Equals("URL"))
            {
                string pattern = @"((https?|ftp|gopher|telnet|file|notes|ms-help):((//)|(\\\\))+[\w\d:#@%/;$()~_?\+-=\\\.&]*)";

                Regex reg = new Regex(pattern);

                //searchs for the regex in the tag value
               
             
                if (reg.IsMatch(kvp.Value))
                {
                    return true;
                }

            }
            else if (regex.Equals("Base64"))
            {
                string pattern = @"[0-9a-zA-Z\+/=]{20,}";

                Regex reg = new Regex(pattern);

                if (reg.IsMatch(kvp.Value))
                {
                    return true;
                }
            }
            else
            {
                Regex reg = null;

                reg = new Regex(@"" + regex);

                if(reg.IsMatch(kvp.Value))
                {
                    return true;
                }
            }

            return false;
        }

        /// <summary>
        /// Extracts the filetype from the path
        /// </summary>
        /// <param name="path">the path of the file</param>
        /// <returns>the filytype</returns>
        private string ExtractFiletype(string path)
        {
            string type = null;

            for (int i = path.Length - 1; i > 0; i--)
            {
                //adds the next char to the string
                type = path[i] + type;

                //ends the function if the next char is a "."
                if ((path[i - 1]) == '.')
                    return type;
            }

            return "unknown";
        }

        private void WindowsTagAnalyzerGUI_Load(object sender, EventArgs e)
        {

        }

        /// <summary>
        /// EventHandler for the Generate Report menu
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void mnuGenerate_Click(object sender, EventArgs e)
        {
            saveReport.Filter = "CSV-File|*.csv";
            
            saveReport.ShowDialog();
            if(!saveReport.FileName.Equals(""))
            {
                DAO.getDAO().writeReport(selectedTreeNode.FullPath, saveReport.FileName);
            }
            
            //MessageBox.Show(saveReport.FileName);
        }

        /// <summary>
        /// Detects if a TreeNode is right clicked and then displays a menu
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void FolderTreeView_NodeMouseClick(object sender, TreeNodeMouseClickEventArgs e)
        {
            if (e.Button == MouseButtons.Right)
            {
                selectedTreeNode = FolderTreeView.GetNodeAt(e.X, e.Y);

                ContextMenuStrip mnu = new ContextMenuStrip();
                ToolStripMenuItem mnuGenerate = new ToolStripMenuItem("Generate Report");

                //Assign event handlers
                mnuGenerate.Click += new EventHandler(mnuGenerate_Click);

                //Add to main context menu
                mnu.Items.AddRange(new ToolStripItem[] { mnuGenerate });
                //Assign to datagridview
                //FolderTreeView.Nodes[0].ContextMenuStrip = mnu;

                selectedTreeNode.ContextMenuStrip = mnu;
            }
        }

        /// <summary>
        /// Kill all resources that are left
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void WindowsTagAnalyzerGUI_FormClosed(object sender, FormClosedEventArgs e)
        {
            //Kill Threads
            if (tagReaderThread != null)
            {
                if (tagReaderThread.IsAlive)
                {
                    this.tagReaderThread.Abort();
                }
            }
            if (treeViewBuilderThread != null)
            {
                if (treeViewBuilderThread.IsAlive)
                {
                    this.treeViewBuilderThread.Abort();
                }
            }

            //Kill Timers
            if (tagReaderTimer != null)
            {
                tagReaderTimer.Stop();
            }            
            if (treeViewBuilderTimer != null)
            {
                treeViewBuilderTimer.Stop();
            }

        }

        /// <summary>
        /// Cancels all running operations (FileTree builder, tag reader)
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void cancelBtn_Click(object sender, EventArgs e)
        {
            //Kill Threads
            if (tagReaderThread != null)
            {
                if (tagReaderThread.IsAlive)
                {
                    this.tagReaderThread.Abort();
                }
            }
            if (treeViewBuilderThread != null)
            {
                if (treeViewBuilderThread.IsAlive)
                {
                    this.treeViewBuilderThread.Abort();
                }
            }
        }

        private void regex_Click(object sender, EventArgs e)
        {
            rx.Show();
        }
    }
}