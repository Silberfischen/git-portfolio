namespace WindowsTagAnalyzer.com.fh.praktikanten.gui
{
    partial class WindowsTagAnalyzerDetails
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
            this.dataGridView1 = new System.Windows.Forms.DataGridView();
            this.FileName = new System.Windows.Forms.Label();
            this.FilePath = new System.Windows.Forms.Label();
            this.GenerateReport = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).BeginInit();
            this.SuspendLayout();
            // 
            // dataGridView1
            // 
            this.dataGridView1.AllowUserToAddRows = false;
            this.dataGridView1.AllowUserToDeleteRows = false;
            this.dataGridView1.AllowUserToResizeColumns = false;
            this.dataGridView1.AllowUserToResizeRows = false;
            this.dataGridView1.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.dataGridView1.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridView1.Location = new System.Drawing.Point(1, 45);
            this.dataGridView1.MinimumSize = new System.Drawing.Size(0, 320);
            this.dataGridView1.Name = "dataGridView1";
            this.dataGridView1.Size = new System.Drawing.Size(802, 320);
            this.dataGridView1.TabIndex = 0;
            this.dataGridView1.Sorted += new System.EventHandler(this.dataGridView1_Sorted);
            // 
            // FileName
            // 
            this.FileName.AutoSize = true;
            this.FileName.Location = new System.Drawing.Point(12, 9);
            this.FileName.Name = "FileName";
            this.FileName.Size = new System.Drawing.Size(35, 13);
            this.FileName.TabIndex = 1;
            this.FileName.Text = "label1";
            // 
            // FilePath
            // 
            this.FilePath.AutoSize = true;
            this.FilePath.Location = new System.Drawing.Point(12, 26);
            this.FilePath.Name = "FilePath";
            this.FilePath.Size = new System.Drawing.Size(35, 13);
            this.FilePath.TabIndex = 3;
            this.FilePath.Text = "label3";
            // 
            // GenerateReport
            // 
            this.GenerateReport.Location = new System.Drawing.Point(668, 14);
            this.GenerateReport.Name = "GenerateReport";
            this.GenerateReport.Size = new System.Drawing.Size(124, 25);
            this.GenerateReport.TabIndex = 4;
            this.GenerateReport.Text = "Generate Report";
            this.GenerateReport.UseVisualStyleBackColor = true;
            // 
            // WindowsTagAnalyzerDetails
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(804, 369);
            this.Controls.Add(this.GenerateReport);
            this.Controls.Add(this.FilePath);
            this.Controls.Add(this.FileName);
            this.Controls.Add(this.dataGridView1);
            this.MaximumSize = new System.Drawing.Size(820, 407);
            this.MinimumSize = new System.Drawing.Size(820, 407);
            this.Name = "WindowsTagAnalyzerDetails";
            this.Text = "Windows Tag Analyzer  - Created By Michael Brey & Thorsten Jojart";
            this.Load += new System.EventHandler(this.WindowsTagAnalyzerDetails_Load);
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.DataGridView dataGridView1;
        private System.Windows.Forms.Label FileName;
        private System.Windows.Forms.Label FilePath;
        private System.Windows.Forms.Button GenerateReport;
    }
}