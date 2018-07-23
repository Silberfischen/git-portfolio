namespace WindowsTagAnalyzer.com.fh.praktikanten.gui
{
    partial class regexGUI
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
            this.regexlist = new System.Windows.Forms.CheckedListBox();
            this.addOwn = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.done = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // regexlist
            // 
            this.regexlist.FormattingEnabled = true;
            this.regexlist.Items.AddRange(new object[] {
            "Base64",
            "URL"});
            this.regexlist.Location = new System.Drawing.Point(12, 34);
            this.regexlist.Name = "regexlist";
            this.regexlist.Size = new System.Drawing.Size(205, 214);
            this.regexlist.TabIndex = 0;
            this.regexlist.ThreeDCheckBoxes = true;
            // 
            // addOwn
            // 
            this.addOwn.Location = new System.Drawing.Point(12, 254);
            this.addOwn.Name = "addOwn";
            this.addOwn.Size = new System.Drawing.Size(75, 23);
            this.addOwn.TabIndex = 1;
            this.addOwn.Text = "Add Own";
            this.addOwn.UseVisualStyleBackColor = true;
            this.addOwn.Click += new System.EventHandler(this.addOwn_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(9, 9);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(130, 18);
            this.label1.TabIndex = 2;
            this.label1.Text = "Choose REGEX...";
            // 
            // done
            // 
            this.done.Location = new System.Drawing.Point(142, 254);
            this.done.Name = "done";
            this.done.Size = new System.Drawing.Size(75, 23);
            this.done.TabIndex = 3;
            this.done.Text = "Done";
            this.done.UseVisualStyleBackColor = true;
            this.done.Click += new System.EventHandler(this.done_Click);
            // 
            // regexGUI
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(229, 290);
            this.Controls.Add(this.done);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.addOwn);
            this.Controls.Add(this.regexlist);
            this.Name = "regexGUI";
            this.Text = "Choose REGEX";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.regexGUI_FormClosing);
            this.Load += new System.EventHandler(this.regexGUI_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.CheckedListBox regexlist;
        private System.Windows.Forms.Button addOwn;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Button done;
    }
}