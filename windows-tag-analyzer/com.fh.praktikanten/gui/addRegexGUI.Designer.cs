namespace WindowsTagAnalyzer.com.fh.praktikanten.gui
{
    partial class addRegexGUI
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
            this.regextextbox = new System.Windows.Forms.TextBox();
            this.confirm = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // regextextbox
            // 
            this.regextextbox.Location = new System.Drawing.Point(12, 23);
            this.regextextbox.Name = "regextextbox";
            this.regextextbox.Size = new System.Drawing.Size(282, 20);
            this.regextextbox.TabIndex = 0;
            // 
            // confirm
            // 
            this.confirm.Location = new System.Drawing.Point(300, 20);
            this.confirm.Name = "confirm";
            this.confirm.Size = new System.Drawing.Size(77, 24);
            this.confirm.TabIndex = 1;
            this.confirm.Text = "Confirm";
            this.confirm.UseVisualStyleBackColor = true;
            this.confirm.Click += new System.EventHandler(this.confirm_Click);
            // 
            // addRegexGUI
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(380, 56);
            this.Controls.Add(this.confirm);
            this.Controls.Add(this.regextextbox);
            this.Name = "addRegexGUI";
            this.Text = "addRegexGUI";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox regextextbox;
        private System.Windows.Forms.Button confirm;
    }
}