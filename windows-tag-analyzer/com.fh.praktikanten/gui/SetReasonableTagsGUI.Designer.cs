namespace WindowsTagAnalyzer.com.fh.praktikanten.gui
{
    partial class SetReasonableTagsGUI
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(SetReasonableTagsGUI));
            this.ListAllTags = new System.Windows.Forms.ListBox();
            this.ListReasonableTags = new System.Windows.Forms.ListBox();
            this.AddTag = new System.Windows.Forms.Button();
            this.RemoveTag = new System.Windows.Forms.Button();
            this.filetype = new System.Windows.Forms.TextBox();
            this.LoadTags = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // ListAllTags
            // 
            this.ListAllTags.FormattingEnabled = true;
            this.ListAllTags.Location = new System.Drawing.Point(12, 76);
            this.ListAllTags.Name = "ListAllTags";
            this.ListAllTags.SelectionMode = System.Windows.Forms.SelectionMode.MultiExtended;
            this.ListAllTags.Size = new System.Drawing.Size(183, 342);
            this.ListAllTags.TabIndex = 0;
            // 
            // ListReasonableTags
            // 
            this.ListReasonableTags.FormattingEnabled = true;
            this.ListReasonableTags.Location = new System.Drawing.Point(259, 76);
            this.ListReasonableTags.Name = "ListReasonableTags";
            this.ListReasonableTags.SelectionMode = System.Windows.Forms.SelectionMode.MultiExtended;
            this.ListReasonableTags.Size = new System.Drawing.Size(183, 342);
            this.ListReasonableTags.TabIndex = 1;
            // 
            // AddTag
            // 
            this.AddTag.Image = ((System.Drawing.Image)(resources.GetObject("AddTag.Image")));
            this.AddTag.Location = new System.Drawing.Point(205, 181);
            this.AddTag.Name = "AddTag";
            this.AddTag.Size = new System.Drawing.Size(41, 36);
            this.AddTag.TabIndex = 2;
            this.AddTag.UseVisualStyleBackColor = true;
            this.AddTag.Click += new System.EventHandler(this.AddTag_Click);
            // 
            // RemoveTag
            // 
            this.RemoveTag.Image = ((System.Drawing.Image)(resources.GetObject("RemoveTag.Image")));
            this.RemoveTag.Location = new System.Drawing.Point(205, 223);
            this.RemoveTag.Name = "RemoveTag";
            this.RemoveTag.Size = new System.Drawing.Size(41, 36);
            this.RemoveTag.TabIndex = 3;
            this.RemoveTag.UseVisualStyleBackColor = true;
            this.RemoveTag.Click += new System.EventHandler(this.RemoveTag_Click);
            // 
            // filetype
            // 
            this.filetype.Location = new System.Drawing.Point(72, 48);
            this.filetype.Name = "filetype";
            this.filetype.Size = new System.Drawing.Size(80, 20);
            this.filetype.TabIndex = 4;
            this.filetype.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.filetype_KeyPress);
            // 
            // LoadTags
            // 
            this.LoadTags.Location = new System.Drawing.Point(158, 45);
            this.LoadTags.Name = "LoadTags";
            this.LoadTags.Size = new System.Drawing.Size(88, 25);
            this.LoadTags.TabIndex = 5;
            this.LoadTags.Text = "Load Tags";
            this.LoadTags.UseVisualStyleBackColor = true;
            this.LoadTags.Click += new System.EventHandler(this.LoadTags_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(140, 9);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(183, 24);
            this.label1.TabIndex = 6;
            this.label1.Text = "Set reasonable Tags";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(22, 51);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(46, 13);
            this.label2.TabIndex = 7;
            this.label2.Text = "Filetype:";
            // 
            // SetReasonableTagsGUI
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(457, 439);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.LoadTags);
            this.Controls.Add(this.filetype);
            this.Controls.Add(this.RemoveTag);
            this.Controls.Add(this.AddTag);
            this.Controls.Add(this.ListReasonableTags);
            this.Controls.Add(this.ListAllTags);
            this.Name = "SetReasonableTagsGUI";
            this.Text = "Set Reasonable Tags - Created By Michael Brey & Thorsten Jojart";
            this.Load += new System.EventHandler(this.SetReasonableTagsGUI_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ListBox ListAllTags;
        private System.Windows.Forms.ListBox ListReasonableTags;
        private System.Windows.Forms.Button AddTag;
        private System.Windows.Forms.Button RemoveTag;
        private System.Windows.Forms.TextBox filetype;
        private System.Windows.Forms.Button LoadTags;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
    }
}