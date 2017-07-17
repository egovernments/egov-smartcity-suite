#!/bin/bash

WORKING_DIR="${HOME}/PHOENIX"

txtrst=$(tput sgr0) # Text reset
txtred=$(tput setaf 1) # Red
txtgrn=$(tput setaf 2) # Green
txtylw=$(tput setaf 3) # Yellow
txtblu=$(tput setaf 4) # Blue
txtpur=$(tput setaf 5) # Purple
txtcyn=$(tput setaf 6) # Cyan
txtwht=$(tput setaf 7) # White
txtbld=$(tput bold) # bold	

chk_user()
{
	if [ "$(id -u)" != "0" ];
	then
		echo -e "\n\t\033[44;37;5m###### WARNING ######\033[0m"
		echo -e "\t${txtylw}${txtbld}Sorry ${txtgrn}$(whoami)${txtrst}${txtylw}${txtbld}, you must login as root user to run this script.${txtrst}" 
		echo -e "\t${txtylw}${txtbld}Please become root user using 'sudo -s' and try again.${txtrst}"
		echo
		echo -e "\t${txtred}${txtbld}Quitting Installer.....${txtrst}\n"
		sleep 1
		exit 1
	fi
}

apt_installer()
{
	echo "Installing ANSIBLE ...!"
	sudo apt-get install software-properties-common
	sudo apt-add-repository ppa:ansible/ansible -y
	sudo apt-get update && sudo apt-get install ansible -y
	run_ansible_playbook;
}

yum_installer()
{
	sudo yum install ansible -y
	run_ansible_playbook;
}

run_ansible_playbook()
{
	echo "Running Playbook to setup the tech stacks ...!"
}

####### MAIN PROGRAME #############
#chk_user
if [ ! -d ${WORKING_DIR} ]
	then
		mkdir ${WORKING_DIR}
	fi

# Os Specifc tweaks do not change anything below ;)
if [ -f /etc/issue ]
then
	OSREQUIREMENT=`cat /etc/issue | awk '{print $1}' | sed 's/Kernel//g' | sed '/^\s*\$/d'`
else
	OSREQ=`cat /etc/redhat-release | awk '{print $1}' | sed 's/Kernel//g' | sed '/^\s*\$/d'`
fi

if [ "${OSREQ}X" = "CentOSX" ]
then
	yum_installer;
else
	case ${OSREQUIREMENT} in
	     "Ubuntu")
				apt_installer;
	           	;;
	     "Red"|"Fedora")
	 			yum_installer;
	           	;;
	     *)
	          	echo -e "\n\t${txtred}${txtbld}###### WARNING ######${txtrst}"
	           	echo -e "\n\t${txtylw}${txtbld}This Script must be executed on${txtrst} ${txtcyn}${txtbld}Ubuntu/Fedora/CentOS/RedHat${txtrst} ${txtylw}${txtbld}Flavor's Only.${txtrst}\n"
	           	exit 1
	           	;;
	esac            
fi
##############  END of Script ############