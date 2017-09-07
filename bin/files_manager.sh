#!/bin/sh

######################################################
##													##
##		Script to execute Files Manager program		##
##													##
##													##
######################################################

#-------------------------------------------------------------
# Usage of this script
#-------------------------------------------------------------
help(){
  echo
  echo
  echo " Help :
    this help         : $0 -h
    $0 -r <generate_report|find_new|find_dup|sync|def_gold_src|undef_gold_src|rename> -s <source_directory> [-t <target_directory>] [-c] [-h]
    -----------------------------------------------------------------------------------------
    -r  : Service to execute
    -s  : Source directory to take into account
    -t  : Target directory to take into account
    -c  : Confirm parameters
    -h  : print this Help
    -----------------------------------------------------------------------------------------"
exit 0
}
#-------------------------------------------------------------
# trace log info
#-------------------------------------------------------------
function logInfo(){
  echo "$(date +%Y%m%d-%T) [INFO] ${1}"
}
#-------------------------------------------------------------
# trace log error
#-------------------------------------------------------------
function logError(){
  echo "$(date +%Y%m%d-%T) [ERROR] ${1}"
}
#-------------------------------------------------------------
# Welcome message
#-------------------------------------------------------------
echo
echo
logInfo "________________________________________________________"
logInfo "________________________________________________________"
logInfo ""
logInfo "            Welcome to Files Manager program !          "
logInfo "________________________________________________________"
logInfo "________________________________________________________"
echo
echo
#-------------------------------------------------------------
# Set default context variables
#-------------------------------------------------------------
{

  service_generate_report="generate_report"
  service_generate_report_desc="Generate complete report for a directory and build its index"

  service_find_new="find_new"
  service_find_new_desc="Find new files between two directories"

  service_find_dup="find_dup"
  service_find_dup_desc="Find duplicate files in a directory"

  service_sync="sync"
  service_sync_desc="Synchronize two directories"

  service_def_gold_src="def_gold_src"
  service_def_gold_src_desc="Define a directory as the golden source one"

  service_undef_gold_src="undef_gold_src"
  service_undef_gold_src_desc="Undefine a directory which is the golden source one"

  service_rename="rename"
  service_rename_desc="Rename files in a directory"

  while getopts r:s:t:ch _option
    do
      case $_option in
        r) SERVICE=$OPTARG
        ;;
        s) SOURCE_DIR=$OPTARG
        ;;
        t) TARGET_DIR=$OPTARG
        ;;
        c) CONFIRM_PARAMETERS=true
        ;;
        h) help && exit 0
        ;;
      esac
  done

  options_valid=true

}
#-------------------------------------------------------------
# Check or request options
#-------------------------------------------------------------
{
 
if [[ "$SERVICE" =~ ^($service_generate_report|$service_find_new|$service_find_dup|$service_sync|$service_def_gold_src|$service_undef_gold_src|$service_rename)$ ]]; then

  [ -z "$SOURCE_DIR" ]    && logError "Source directory is mandatory. Option -s !" && options_valid=false

  if [[ $options_valid == true ]]; then

    [ ! -d "$SOURCE_DIR" ]  && logError "Source directory [$SOURCE_DIR] does NOT exist. Option -s !" && options_valid=false

  fi

  if [[ "$SERVICE" =~ ^($service_find_new|$service_sync)$ ]]; then

    [ -z "$TARGET_DIR" ]    && logError "Target directory is mandatory. Option -t !" && options_valid=false

    if [[ $options_valid == true ]]; then

      [ ! -d "$TARGET_DIR" ]  && logError "Target directory [$TARGET_DIR] does NOT exist. Option -t !" && options_valid=false
      [ "$TARGET_DIR"  == "$SOURCE_DIR" ]  && logError "Target directory and source directory MUST be different !" && options_valid=false

    fi

  fi

elif [[ ! -z "$SERVICE" ]]; then

  echo "Service specified [$SERVICE] is unknown !"
  options_valid=false

else

  service_valid=false

  while ! $service_valid
  do
    echo
    echo "    => Select service you want to execute :"
    echo
    echo "      1) $service_generate_report_desc"
    echo "      2) $service_find_new_desc"
    echo "      3) $service_find_dup_desc"
    echo "      4) $service_sync_desc"
    echo "      5) $service_def_gold_src_desc"
    echo "      6) $service_undef_gold_src_desc"
    echo "      7) $service_rename_desc"
    echo

    read SERVICE

    if [[ $SERVICE =~ ^[1-7]$ ]]; then

      service_valid=true

      case $SERVICE in
        1) SERVICE=$service_generate_report
         ;;
        2) SERVICE=$service_find_new
         ;;
        3) SERVICE=$service_find_dup
         ;;
        4) SERVICE=$service_sync
         ;;
        5) SERVICE=$service_def_gold_src
         ;;
        6) SERVICE=$service_undef_gold_src
         ;;
        7) SERVICE=$service_rename
         ;;
      esac

    else
      echo
      echo "/!\ Service [$SERVICE] is NOT valid, please retry !"
      echo
    fi

  done

  source_dir_valid=false

  while ! $source_dir_valid
    do
      echo
      echo
      echo "    => What is the source directory ?"
      echo

      read SOURCE_DIR

      if [[ -d "$SOURCE_DIR" ]]; then
        source_dir_valid=true
      else
        echo
        echo "/!\ Source directory [$SOURCE_DIR] does NOT exist, please retry !"
        echo
      fi

  done

  if [[ "$SERVICE" =~ ^($service_find_new|$service_sync)$ ]]; then

    target_dir_valid=false

    while ! $target_dir_valid
      do
        echo
        echo
        echo "    => What is the target directory ?"
        echo

       read TARGET_DIR

        if [ -d "$TARGET_DIR" ] && [ "$TARGET_DIR" != "$SOURCE_DIR" ]; then
          target_dir_valid=true
        elif [ "$TARGET_DIR" == "$SOURCE_DIR" ]; then
          echo
          echo "/!\ Target directory and source directory MUST be different, please retry !"
          echo
        else
          echo
          echo "/!\ Target directory [$TARGET_DIR] does NOT exist, please retry !"
          echo
        fi

    done

  fi

fi
 
  [[ $options_valid == false ]] && help && exit 1

}
#-------------------------------------------------------------
# Confirm parameters
#-------------------------------------------------------------
{
  case $SERVICE in
    $service_generate_report)	service_desc=$service_generate_report_desc
     ;;
    $service_find_new)		service_desc=$service_find_new_desc
     ;;
    $service_find_dup)		service_desc=$service_find_dup_desc
     ;;
    $service_sync)			service_desc=$service_sync_desc
     ;;
    $service_def_gold_src)	service_desc=$service_def_gold_src_desc
     ;;
    $service_undef_gold_src)	service_desc=$service_undef_gold_src_desc
     ;;
    $service_rename)			service_desc=$service_rename_desc
     ;;
  esac

  echo
  echo
  logInfo "________________________________________________________"
  logInfo "                                                        "
  logInfo "                       Parameters                       "
  logInfo "________________________________________________________"
  logInfo ""
  logInfo "--> Service to execute ................ $service_desc"
  logInfo "--> Source directory .................. $SOURCE_DIR"
  if [[ ! -z "$TARGET_DIR" ]]; then
    logInfo "--> Target directory .................. $TARGET_DIR"
  fi
  logInfo "________________________________________________________"

  if [[ "$CONFIRM_PARAMETERS" == true ]]; then
    confirmation_valid=true;
    confirmation_execute="y";
  else
    confirmation_valid=false;
  fi

  while ! $confirmation_valid
    do
      echo
      echo
      echo "    => DO YOU WANT TO EXECUTE FILES MANAGER PROGRAM WITH THOSE PARAMETERS [y/n] ?"
      echo

      read confirmation_execute

      if [[ $confirmation_execute =~ ^[yn]$ ]]; then
        confirmation_valid=true
      else
        echo
        echo "/!\ Response [$confirmation_execute] is NOT valid, please retry !"
        echo
      fi

  done

  if [ $confirmation_execute == "n" ]; then
    exit 0
  fi

}
#-------------------------------------------------------------
# Call Files Manager JAVA program
#-------------------------------------------------------------
cd "$(dirname "$0")/.."
echo
echo

args="-r \"$SERVICE\" -s \"$SOURCE_DIR\""
[ -d "$TARGET_DIR" ] && args="$args -t \"$TARGET_DIR\""

jar_name="lib/files-manager.jar"
java_opts="-Xms2048m -Xmx4096m -XX:NewSize=256m -XX:MaxNewSize=256m -XX:+DisableExplicitGC"
command="java $java_opts -Dlog4j.configuration=config/log4j.xml -jar $jar_name $args"
echo $command
eval $command
#-------------------------------------------------------------
# END OF SCRIPT
#-------------------------------------------------------------