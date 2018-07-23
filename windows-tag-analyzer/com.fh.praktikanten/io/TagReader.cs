using System.Collections.Generic;
using Shell32;
using WindowsTagAnalyzer.com.fh.praktikanten.db;
using System.IO;
using System;
using System.Windows.Forms;
using System.Threading;


namespace WindowsTagAnalyzer.com.fh.praktikanten.io
{
    public class TagReader
    {
        /// <summary>
        /// All tags which are not empty
        /// </summary>
        private Dictionary<string, string> usedTags = new Dictionary<string, string>();

        /// <summary>
        /// Dictionary to store all the hashes
        /// </summary>
        private Dictionary<string, string> hashes = new Dictionary<string, string>();

        /// <summary>
        /// the filePath for report generation
        /// </summary>
        string absoluteFilePath;

        /// <summary>
        /// The Shell object used to read the file infos
        /// </summary>
        private static Shell sh = new Shell();

        /// <summary>
        /// A List of all tags
        /// </summary>
        public static List<string> allTags;

        private static List<TagReader> allTagReaders = new List<TagReader>();

        /// <summary>
        /// Populate the allTagsList when the class is loaded
        /// </summary>
        static TagReader()
        {
            TagReader.refreshTags();
        }

        /// <summary>
        /// Creates a new TagReader with all information about the tags
        /// </summary>
        /// <param name="folder">The Folder object which is used to read the tags from the file</param>
        /// <param name="absoluteFilePath">the path of the file</param>
        private TagReader(Folder folder, string absoluteFilePath)
        {
            //Get the actual FileDescription
            FolderItem file = folder.ParseName(Path.GetFileName(absoluteFilePath));
            //helper
            string tagValue = "";


            for (int i = 0; i < allTags.Count; i++)
            {
                //retrieve tag nr. 1-284
                tagValue = folder.GetDetailsOf(file, i);
                if (tagValue != "")
                {
                    //Add tag to usedTags
                    usedTags.Add(allTags[i], tagValue);
                }
                else
                {
                    //Next one!
                    continue;
                }
            }
            //Calculate all hashes
            hashes.Add("MD5", FileHasher.getMD5(absoluteFilePath));
            hashes.Add("SHA1", FileHasher.getSHA1(absoluteFilePath));
            //hashes.Add("SHA256", FileHasher.getSHA256(absoluteFilePath));
            //hashes.Add("SHA384", FileHasher.getSHA384(absoluteFilePath));
            //hashes.Add("SHA512", FileHasher.getSHA512(absoluteFilePath));

            this.absoluteFilePath = absoluteFilePath;

            //Try to apply threading
            //Thread thread = new Thread(delegate() { DAO.getDAO().saveReportParams(this); });
            //thread.Start();

            //save report about the file to the db
            DAO.getDAO().saveReportParams(this);
        }

        /// <summary>
        /// Uses C:\\ to retrieve all possible Tags a File/Folder can have
        /// </summary>
        /// <returns>The tags as a generic Dictionary in the form int,string</returns>
        public static Dictionary<int, string> getAllTags()
        {
            //The dictionary which will contain all possible tags
            Dictionary<int, string> allTags = new Dictionary<int, string>();

            //Get c:\\ ready to read hard
            Folder folder = sh.NameSpace("c:\\");

            string propertyName = "";

            //Read all the properties
            for (int i = 0; i < int.MaxValue; i++)
            {
                //Get possible tags
                propertyName = folder.GetDetailsOf(null, i);

                if ("".Equals(propertyName))
                {
                    //No tags are left to read
                    break;
                }

                //propertyName is not empty -> new tag discovered
                allTags.Add(i + 1, propertyName);
            }
            return allTags;
        }

        /// <summary>
        /// Traverses a Directory recursive and uses a TagReader List as a Wrapper for alle the used tags
        /// </summary>
        /// <param name="directory"></param>
        /// <returns></returns>
        public static List<TagReader> getAllTagReadersFromDirectory(DirectoryInfo directory)
        {

            System.IO.FileInfo[] allFiles = null;
            System.IO.DirectoryInfo[] subDirectories = null;

            Folder shellFolder = null;

            try
            {
                //get all Files from the current directory
                allFiles = directory.GetFiles();
            }

            catch (UnauthorizedAccessException e)
            {
                //Oops, you should have more rights
                //MessageBox.Show("You have no Access to the directory: \"" + directory.FullName + "\"!");
            }
            catch (System.IO.DirectoryNotFoundException e)
            {
                //When this happens, something, somewhere, totally went wrong
                MessageBox.Show("The directory: \"" + directory.FullName + "\" could not be found!");
            }

            //There are more Files to proceed
            if (allFiles != null)
            {
                //Save TagReaders
                foreach (System.IO.FileInfo fi in allFiles)
                {
                    try
                    {
                        //Create ShellFolder
                        shellFolder = sh.NameSpace(fi.DirectoryName);
                    }
                    catch (Exception e)
                    {
                        //MessageBox.Show(e.ToString());
                    }
                    //Happens sometimes to folders like 
                    //C:\$Recycle.Bin\S-1-5-21-1712340149-1785855708-1618849827-1001\$RV05Q8E\sqliteLib\
                    //Windows Explorer is also unable to open them
                    if (shellFolder != null)
                    {
                        //Add a new TagReader to the list
                        TagReader.allTagReaders.Add(new TagReader(shellFolder, fi.FullName));
                    }
                }

                // Now find all the subdirectories under this directory.
                subDirectories = directory.GetDirectories();

                foreach (System.IO.DirectoryInfo dirInfo in subDirectories)
                {
                    // Resursive call for each subdirectory.
                    getAllTagReadersFromDirectory(dirInfo);
                }
            }

            return TagReader.allTagReaders;
        }

        /// <summary>
        /// Traverses a Directory recursive and uses a TagReader List as a Wrapper for alle the used tags
        /// </summary>
        /// <param name="directory"></param>
        /// <returns></returns>
        public static List<TagReader> getAllTagReadersFromDirectory(DirectoryInfo directory,WindowsTagAnalyzer.com.fh.praktikanten.gui.WindowsTagAnalyzerGUI gui)
        {
            gui.SetProgressTagReader("Scanning... " + directory.FullName);

            System.IO.FileInfo[] allFiles = null;
            System.IO.DirectoryInfo[] subDirectories = null;

            Folder shellFolder = null;

            //List to store all tag readers
            List<TagReader> tagReaders = new List<TagReader>();

            try
            {
                //get all Files from the current directory
                allFiles = directory.GetFiles();
            }

            catch (UnauthorizedAccessException e)
            {
                //Oops, you should have more rights
                //MessageBox.Show("You have no Access to the directory: \"" + directory.FullName + "\"!");
            }
            catch (System.IO.DirectoryNotFoundException e)
            {
                //When this happens, something, somewhere, totally went wrong
                MessageBox.Show("The directory: \"" + directory.FullName + "\" could not be found!");
            }

            //There are more Files to proceed
            if (allFiles != null)
            {
                //Save TagReaders
                foreach (System.IO.FileInfo fi in allFiles)
                {
                    try
                    {
                        //Create ShellFolder
                        shellFolder = sh.NameSpace(fi.DirectoryName);
                    }
                    catch (Exception e)
                    {
                        //MessageBox.Show(e.ToString());
                    }
                    //Happens sometimes to folders like 
                    //C:\$Recycle.Bin\S-1-5-21-1712340149-1785855708-1618849827-1001\$RV05Q8E\sqliteLib\
                    //Windows Explorer is also unable to open them
                    if (shellFolder != null)
                    {
                        TagReader tr = new TagReader(shellFolder, fi.FullName);

                        //Add a new TagReader to the list
                        //Do this synchronized because then the gui dont have to wait for the end of this function
                        TagReader.allTagReaders.Add(new TagReader(shellFolder, fi.FullName));

                    }
                }

                // Now find all the subdirectories under this directory.
                subDirectories = directory.GetDirectories();

                foreach (System.IO.DirectoryInfo dirInfo in subDirectories)
                {
                    // Resursive call for each subdirectory.
                    getAllTagReadersFromDirectory(dirInfo,gui);
                }
            }

            return TagReader.allTagReaders;
        }

        /// <summary>
        /// Getter for the used tags of the file
        /// </summary>
        /// <returns>All used tags as a generic Dictionary in the form string,string</returns>
        public Dictionary<string, string> getUsedTags()
        {
            return usedTags;
        }

        /// <summary>
        /// Getter for the hashes
        /// </summary>
        /// <returns>All used tags as a generic Dictionary in the form string,string</returns>
        public Dictionary<string, string> getHashes()
        {
            return hashes;
        }

        /// <summary>
        /// Getter for the absolute path of the file
        /// </summary>
        /// <returns>The absolute path as a string</returns>
        public string getAbsoluteFilePath()
        {
            return this.absoluteFilePath;
        }

        /// <summary>
        /// Pulls the latest information out of the db
        /// </summary>
        public static void refreshTags()
        {
            TagReader.allTags = DAO.getDAO().getAllTags();
        }
    }
}