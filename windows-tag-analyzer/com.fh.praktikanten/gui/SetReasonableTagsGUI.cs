using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using WindowsTagAnalyzer.com.fh.praktikanten.db;

namespace WindowsTagAnalyzer.com.fh.praktikanten.gui
{
    public partial class SetReasonableTagsGUI : Form
    {
        public SetReasonableTagsGUI()
        {
            InitializeComponent();
            FillLeftBox();
        }


        /// <summary>
        /// Fills the Left box with all tags from the db
        /// </summary>
        private void FillLeftBox()
        {
            //Get all tags out of the db
            List<string> allTags = DAO.getDAO().getAllTags();

            //Clear the ListBox if the Db-operation was successfull    
            ListAllTags.Items.Clear();
            
            foreach (string tag in allTags)
            {
                //Add all tags to the listbox
                ListAllTags.Items.Add(tag);
            }

            //Sorts the tags in alphabetical order
            ListAllTags.Sorted = true;
            ListReasonableTags.Sorted = true;
        }

        /// <summary>
        /// Fills the right box width the reasonable tags from the DB
        /// </summary>
        private void FillRightBox()
        {
            //Get reasonable tags out of the db
            List<string> reasonableTags = DAO.getDAO().getReasonableTags(filetype.Text);

            //Clear the ListBox if the Db-operation was successfull    
            ListReasonableTags.Items.Clear();

            foreach (string tag in reasonableTags)
            {
                //Add reasonable tags to the listbox
                ListReasonableTags.Items.Add(tag);
            }

            //Sorts the tags in alphabetical order
            ListAllTags.Sorted = true;
            ListReasonableTags.Sorted = true;
        }

        /// <summary>
        /// Removes all tags that are in the reasonable ListBox and in the all ListBox
        /// </summary>
        private void RemoveRedundantTags()
        {
            String strItem;

            foreach (Object selecteditem in ListReasonableTags.Items)
            {
                strItem = selecteditem as String;
                ListAllTags.Items.Remove(strItem);
            }
        }

        /// <summary>
        /// Adds the tags which are selected to the reasonable tags of the selected filetype
        /// Then the tags which are in the reasonable tags list are removed from the list of all tags
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void AddTag_Click(object sender, EventArgs e)
        {
            String strItem;
            String[] selectedValuestoRemove = new String[ListAllTags.SelectedItems.Count];
            int count = 0;
            Dictionary<string, string> valuestoInsert = new Dictionary<string,string>();


            if (ListAllTags.SelectedItems != null)
            {
                //Adds the selected tags one by one to the reasonable tags list
                foreach (Object selecteditem in ListAllTags.SelectedItems)
                {
                    strItem = selecteditem as String;
                    ListReasonableTags.Items.Add(strItem);

                    //Stores the values of the items that have to be deleted
                    selectedValuestoRemove[count] = strItem;
                    count++;

                    //Stores the values that have to be added in the DB
                    valuestoInsert.Add(strItem, filetype.Text);
                }

                //Adds the values of the new reasonable tags to the DB
                DAO.getDAO().insertReasonableTags(valuestoInsert);

                //Sorts the tags in alphabetical order
                ListAllTags.Sorted = true;
                ListReasonableTags.Sorted = true;

                //Removes the tags, that are added to the reasonable tags list, from the List with all tags
                foreach (String stringtoRemove in selectedValuestoRemove)
                {
                    ListAllTags.Items.Remove(stringtoRemove);
                }
            }
        }


        /// <summary>
        /// Removes Tags from the reasonable List
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void RemoveTag_Click(object sender, EventArgs e)
        {
            String strItem;
            String[] selectedValuestoRemove = new String[ListReasonableTags.SelectedItems.Count];
            int count = 0;
            Dictionary<string, string> valuestoRemove = new Dictionary<string, string>();

            if (ListReasonableTags.SelectedItems != null)
            {
                //Adds the selected tags one by one to the list with all tags
                foreach (Object selecteditem in ListReasonableTags.SelectedItems)
                {
                    strItem = selecteditem as String;
                    ListAllTags.Items.Add(strItem);

                    //Stores the values of the items that have to be deleted
                    selectedValuestoRemove[count] = strItem;
                    count++;

                    //Stores the values that have to be removed in the DB
                    valuestoRemove.Add(strItem, filetype.Text);
                }

                //Removes the values of the new reasonable tags to the DB
                DAO.getDAO().deleteReasonableTags(valuestoRemove);

                //Sorts the tags in alphabetical order
                ListAllTags.Sorted = true;
                ListReasonableTags.Sorted = true;

                //Removes the tags from the reasonable tags list
                foreach (String stringtoRemove in selectedValuestoRemove)
                {
                    ListReasonableTags.Items.Remove(stringtoRemove);
                }
            }
        }

        /// <summary>
        /// When the Load Tags button is clicked the left and the right box are filled with the tags and 
        /// the redundant tags will be removed
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void LoadTags_Click(object sender, EventArgs e)
        {
            FillLeftBox();
            FillRightBox();
            RemoveRedundantTags();
        }

        private void SetReasonableTagsGUI_Load(object sender, EventArgs e)
        {

        }

        /// <summary>
        /// Proofs if the enter key is pressed
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void filetype_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == (char)13)
            {
                FillLeftBox();
                FillRightBox();
                RemoveRedundantTags();
            }
        }

    }
}
