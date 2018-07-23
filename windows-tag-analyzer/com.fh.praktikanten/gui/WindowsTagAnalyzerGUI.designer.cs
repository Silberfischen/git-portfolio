using WindowsTagAnalyzer.com.fh.praktikanten.db;

namespace WindowsTagAnalyzer.com.fh.praktikanten.gui
{
    partial class WindowsTagAnalyzerGUI
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(WindowsTagAnalyzerGUI));
            this.ChooseDirectory = new System.Windows.Forms.Button();
            this.RefreshButton = new System.Windows.Forms.Button();
            this.SetTags = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.cancelBtn = new System.Windows.Forms.Button();
            this.OpenFolder = new System.Windows.Forms.FolderBrowserDialog();
            this.FolderTreeView = new System.Windows.Forms.TreeView();
            this.FolderTreeViewImages = new System.Windows.Forms.ImageList(this.components);
            this.progressLabelTreeView = new System.Windows.Forms.Label();
            this.progressLabelTagReader = new System.Windows.Forms.Label();
            this.saveReport = new System.Windows.Forms.SaveFileDialog();
            this.regex = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // ChooseDirectory
            // 
            this.ChooseDirectory.Location = new System.Drawing.Point(12, 41);
            this.ChooseDirectory.Name = "ChooseDirectory";
            this.ChooseDirectory.Size = new System.Drawing.Size(102, 26);
            this.ChooseDirectory.TabIndex = 2;
            this.ChooseDirectory.Text = "Choose Directory";
            this.ChooseDirectory.UseVisualStyleBackColor = true;
            this.ChooseDirectory.Click += new System.EventHandler(this.ChooseDirectory_Click);
            // 
            // RefreshButton
            // 
            this.RefreshButton.Location = new System.Drawing.Point(120, 41);
            this.RefreshButton.Name = "RefreshButton";
            this.RefreshButton.Size = new System.Drawing.Size(89, 26);
            this.RefreshButton.TabIndex = 3;
            this.RefreshButton.Text = "Refresh";
            this.RefreshButton.UseVisualStyleBackColor = true;
            this.RefreshButton.Click += new System.EventHandler(this.RefreshButton_Click);
            // 
            // SetTags
            // 
            this.SetTags.Location = new System.Drawing.Point(677, 41);
            this.SetTags.Name = "SetTags";
            this.SetTags.Size = new System.Drawing.Size(115, 26);
            this.SetTags.TabIndex = 5;
            this.SetTags.Text = "Set reasonable tags";
            this.SetTags.UseVisualStyleBackColor = true;
            this.SetTags.Click += new System.EventHandler(this.SetTags_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 15.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(295, 9);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(232, 25);
            this.label1.TabIndex = 6;
            this.label1.Text = "Windows Tag Analyzer";
            // 
            // cancelBtn
            // 
            this.cancelBtn.Location = new System.Drawing.Point(570, 41);
            this.cancelBtn.Name = "cancelBtn";
            this.cancelBtn.Size = new System.Drawing.Size(101, 26);
            this.cancelBtn.TabIndex = 8;
            this.cancelBtn.Text = "Cancel Scanning";
            this.cancelBtn.UseVisualStyleBackColor = true;
            this.cancelBtn.Click += new System.EventHandler(this.cancelBtn_Click);
            // 
            // FolderTreeView
            // 
            this.FolderTreeView.Location = new System.Drawing.Point(13, 73);
            this.FolderTreeView.Name = "FolderTreeView";
            this.FolderTreeView.Size = new System.Drawing.Size(779, 393);
            this.FolderTreeView.TabIndex = 9;
            this.FolderTreeView.NodeMouseClick += new System.Windows.Forms.TreeNodeMouseClickEventHandler(this.FolderTreeView_NodeMouseClick);
            this.FolderTreeView.NodeMouseDoubleClick += new System.Windows.Forms.TreeNodeMouseClickEventHandler(this.FolderTreeView_NodeMouseDoubleClick);
            // 
            // FolderTreeViewImages
            // 
            this.FolderTreeViewImages.ImageStream = ((System.Windows.Forms.ImageListStreamer)(resources.GetObject("FolderTreeViewImages.ImageStream")));
            this.FolderTreeViewImages.TransparentColor = System.Drawing.Color.Transparent;
            this.FolderTreeViewImages.Images.SetKeyName(0, "file_icon.jpg");
            this.FolderTreeViewImages.Images.SetKeyName(1, "foldericon.jpg");
            // 
            // progressLabelTreeView
            // 
            this.progressLabelTreeView.AutoSize = true;
            this.progressLabelTreeView.Location = new System.Drawing.Point(12, 473);
            this.progressLabelTreeView.Name = "progressLabelTreeView";
            this.progressLabelTreeView.Size = new System.Drawing.Size(33, 13);
            this.progressLabelTreeView.TabIndex = 10;
            this.progressLabelTreeView.Text = "^sdfg";
            // 
            // progressLabelTagReader
            // 
            this.progressLabelTagReader.AutoSize = true;
            this.progressLabelTagReader.Location = new System.Drawing.Point(12, 486);
            this.progressLabelTagReader.Name = "progressLabelTagReader";
            this.progressLabelTagReader.RightToLeft = System.Windows.Forms.RightToLeft.No;
            this.progressLabelTagReader.Size = new System.Drawing.Size(25, 13);
            this.progressLabelTagReader.TabIndex = 11;
            this.progressLabelTagReader.Text = "zrsz";
            // 
            // regex
            // 
            this.regex.Location = new System.Drawing.Point(216, 41);
            this.regex.Name = "regex";
            this.regex.Size = new System.Drawing.Size(125, 26);
            this.regex.TabIndex = 12;
            this.regex.Text = "Choose REGEX";
            this.regex.UseVisualStyleBackColor = true;
            this.regex.Click += new System.EventHandler(this.regex_Click);
            // 
            // WindowsTagAnalyzerGUI
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(804, 501);
            this.Controls.Add(this.regex);
            this.Controls.Add(this.progressLabelTagReader);
            this.Controls.Add(this.progressLabelTreeView);
            this.Controls.Add(this.FolderTreeView);
            this.Controls.Add(this.cancelBtn);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.SetTags);
            this.Controls.Add(this.RefreshButton);
            this.Controls.Add(this.ChooseDirectory);
            this.Name = "WindowsTagAnalyzerGUI";
            this.Text = "Windows Tag Analyzer  - Created By Michael Brey & Thorsten Jojart";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.WindowsTagAnalyzerGUI_FormClosed);
            this.Load += new System.EventHandler(this.WindowsTagAnalyzerGUI_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button ChooseDirectory;
        private System.Windows.Forms.Button RefreshButton;
        private System.Windows.Forms.Button SetTags;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Button cancelBtn;
        private System.Windows.Forms.FolderBrowserDialog OpenFolder;
        private System.Windows.Forms.TreeView FolderTreeView;
        private System.Windows.Forms.ImageList FolderTreeViewImages;
        private System.Windows.Forms.Label progressLabelTreeView;
        private System.Windows.Forms.Label progressLabelTagReader;
        private System.Windows.Forms.SaveFileDialog saveReport;
        private System.Windows.Forms.Button regex;


    }
}