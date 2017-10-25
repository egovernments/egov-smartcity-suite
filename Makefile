#   Phoenix stack setup, build code base and deployer tool.
#
#   Targets (see each target for more information):
#   all: install/build/deploy.
#	install : Install environment stacks
#   build: Build code.
#   deploy: Deploy the EAR to WILDFLY.
#   clean: Remove old EAR from Wildfly.
#   remove: Remove all the stacks.

WORK_DIR	=	${HOME}/EGOV-PHOENIX-STACK
###############

#-include: ./dev-utils/setenv.sh

all: install build deploy
.PHONY: all

###############
.PHONY: install
install:
	@echo "[ ** INSTALL ** ] - Install PHOENIX Stack"
	@eval ./dev-utils/ansible/install.sh ${WORK_DIR}
###############
.PHONY: build
build:
	@echo "[ ** BUILD ** ] - Build PHOENIX code base"
	@eval ./dev-utils/build.sh
###############
.PHONY: deploy
deploy: 
	@echo "[ ** DEPLOY ** ] - Deploy the EAR artifacts to WILDFLY"
	@eval ./dev-utils/deployment/deploy.sh
###############
.PHONY: clean
clean:
	@echo "[ ** CLEAN ** ] - Remove old deployments."
	@eval ./dev-utils/deployment/deploy.sh "clean"
###############
.PHONY: remove
remove:
	@echo "[ ** Remove ** ] - Remove all the stacks."
	@rm -rf ${WORK_DIR}