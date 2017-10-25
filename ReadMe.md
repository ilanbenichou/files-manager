# Files Manager

[![standard-readme compliant](https://img.shields.io/badge/readme%20style-standard-brightgreen.svg?style=flat-square)](https://github.com/ilanbenichou/files-manager)

##### Project website: [https://github.com/ilanbenichou/files-manager](https://github.com/ilanbenichou/files-manager)

***

## Developed by

* Ilan Benichou / French website [https://www.ilan-benichou.com](https://www.ilan-benichou.com)

***

## Description

The Files Manager project will help you to easily **manage your files** thanks to its several features:

* Generate **complete reports** of your directories
* Find **duplicate** files in a given directory
* Find **new/existing** files between 2 directories
* **Synchronize** 2 directories
* **Rename** all files in a directory

It uses the approach of files hash (SHA-1) by scanning their content. For audio files, the audio part is also extracted for hash calculation. 

***

## Requirements

* [JAVA 8 SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Apache Maven](https://maven.apache.org/download.cgi#)

***

## Distribution

Run the following **maven command** to generate application distribution zip file:

```
$ mvn clean package
```

File ***target/files-manager-1.0.1-dist.zip*** will be generated.

***

## Configuration

Configuration file ***config/files-manager.properties*** contains following parameters:

* **waiting_time_to_write_index_file_sec**:
    * Waiting time before writing index file (in seconds) when indexing a directory

* **reports_directory_path**:
    * Directory path where the reports must be generated

* **reports_resources_directory_path**:
    * Resources directory path containing css, javascript files and images for HTML generated reports (this path is copied as is in HTML reports)

***

## Usage

After unzipping distribution file, run ***bin/files_manager.sh*** on your MacOs/Unix system or on your Unix-like environment for Windows system.

The following window will appear:

```
20171005-21:38:28 [INFO] ________________________________________________________
20171005-21:38:28 [INFO] ________________________________________________________
20171005-21:38:28 [INFO] 
20171005-21:38:28 [INFO]             Welcome to Files Manager program !          
20171005-21:38:28 [INFO] ________________________________________________________
20171005-21:38:28 [INFO] ________________________________________________________



    => Select service you want to execute :

      1) Generate complete report for a directory and build its index
      2) Find new files between two directories
      3) Find duplicate files in a directory
      4) Synchronize two directories
      5) Define a directory as the golden source one
      6) Undefine a directory which is the golden source one
      7) Rename files in a directory
```

1. Choose the **service you want to execute** (values 1 to 7)
2. Enter the **source directory**
3. Enter the **target directory** (only for services 2 and 4)
4. **Confirm** by typing “y” for yes if everything seems ok

***

## Features details

### Service 1: Generate a complete HTML report for a given directory and build its index

This service will scan the given directory, build (create or update) its index and generate a report with the following informations:

* Directory size
* Number of files
* List of sub-directories with their size and number of files
* List of files mime types
* List of files with their last update date, size and mime type

This service will also **reinitialize all created directories** by the other services.

#

### Service 2: Find new/existing files between 2 given directories

This service will scan 2 given directories, build their index and find new and existing files.

* **Source directory**  is the directory where you want to find new and existing files
* **Target directory** is the reference directory
* **Existing files will be moved to a created sub-directory** in the given source directory maintaining files tree
* **Only new files will NOT be moved**

The 2 sub-directories that can be created are:

* **__FM_OLD__**:
    * For files that exist in the destination directory with ***EXACTLY THE SAME CONTENT***
* **__FM_OLD_META_DIFF__**:
    * For audio files that exist in the destination directory with ***EXACTLY THE SAME AUDIO PART*** but with different metadatas (like title, album etc ...)

The generated report will contain the following informations:

* Directories size
* Number of files in each directory
* Number of new files
* Number of existing files
* Disk space lost
* List of new files with their last update date, size and mime type
* List of existing files with their last update date, size and mime type

This service will also **reinitialize all created directories** by the other services.

#

### Service 3: Find duplicate files in a given directory

This service will scan the given directory build its index and find duplicate files.

* **Duplicate files will be moved to a sub-directory** (which is duplicate file number) of a sub-directory in the given directory maintaining files tree.
* **Unique files** will NOT be moved

The 2 sub-directories that can be created are:

* __FM_DUP__:
    * For duplicate files that have ***EXACTLY THE SAME CONTENT***
* __FM_DUP_META_DIFF__:
    * For audio files that have ***EXACTLY THE SAME AUDIO PART*** but with different metadatas (like title, album etc ...)

```
Let us take an example with the 3 files located in myDirectory with same audio part and different metadatas:

    - a/b/c/myFile1.mp3 => will be moved to myDirectory/__FM_DUP_META_DIFF__/1/a/b/c/myFile1.mp3
    - d/e/myFile2.mp3   => will be moved to myDirectory/__FM_DUP_META_DIFF__/2/d/e/myFile2.mp3	
    - f/myFile3.mp3     => will be moved to myDirectory/__FM_DUP_META_DIFF__/3/f/myFile3.mp3	
```

The generated report will contain the following informations:

* Directory size
* Number of files
* Number of duplicate file pairs
* Number of duplicate files
* Number of unique files
* Disk space lost
* List of duplicate files with their last update date, size and mime type for files that have **EXACTLY THE SAME CONTENT**
* List of duplicate files with their last update date, size, mime type and metadatas differences for files that have ***EXACTLY THE SAME AUDIO PART*** but with different metadatas

This service will also **reinitialize all created directories** by the other services.

#

### Service 4: Synchronize 2 directories

This service will scan 2 given directories, build their index and synchronize them.

* **Source directory**  is the reference directory
* **Target directory** is the directory you want to synchronize (**/!\ SOME FILES COULD BE CHANGED OR DELETED**)
* **New files** will be copied from source to target directory
* **Updated files** will be copied from source and replaced in target directory
* **Deleted files** of source directory will be also be deleted in target directory

The generated report will contain the following informations:

* Directories size
* Number of files in each directory
* Number of new files
* Number of different files
* Number of deleted files
* Number of existing files
* List of new files with their last update date, size and mime type
* List of different files with their last update date, size and mime type
* List of deleted files with their last update date, size and mime type
* List of existing files with their last update date, size and mime type

This service will also **reinitialize all created directories** by the other services.

#

### Service 5: Define a directory as the golden source one

This service will define a directory as a golden source one.

It means that if a directory is defined as is, the Files Manager application **could NOT change** its content.

```
For example, if by mistake we run the synchronize service and reverse source and target directories:
    => Some files could be deleted because they don't exist in source directory.

So, with the target directory defined as a golden source one, it can NOT happen.
We will have the following message:
    => Unable to execute this service because [target] directory [myDirectory] is the golden source directory !
```

When a directory is defined as a golden source one, a file named ***.fmgoldsrc*** is created at the root of the concerned directory.

#

### Service 6: Undefine a directory as the golden source one

This service will undefine a directory which was a golden source one.

/!\ Be careful, some services **could change directories content**

#

### Service 7: Rename all files in a given directory

This service will rename all files present in a directory by prefixing their name with an underscore.

* The index is not built for this directory
* If a directory is a golden source one, files will NOT be renamed

***

## Further information

### Index:
An index is a list of data, such as database entries. It is typically saved in a plain text format that can be quickly scanned by a search algorithm. This significantly speeds up searching and sorting operations on data referenced by the index. Indexes often include information about each item in the list, such as metadata or keywords, that allows the data to be searched via the index instead of reading through each file individually.

Here an index has the same role, scanning a directory when an index has ever been created make the action faster.

Creation of an index must take a long time, it depends on the number of files and files size.

An index is a file named ***.fmindex*** created at the root of the concerned directory and containing all files informations (relative path, last update date, size, hash and audio hash).

When a directory is rescan, hash calculation will be done for only new and updated files and the index will be updated.

#

### Empty directories:

Empty directories are deleted for all services.

#

### HTML lists:

All the lists into the HTML reports can be sorted and filtered.
