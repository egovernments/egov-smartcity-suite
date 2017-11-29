# Maven ansible role

Role Variables
--------------

[defaults/main.yml](defaults/main.yml)

|*Variable*  | *Default Value* |*Description* |
| --- | --- | --- |
| maven_version | 3.3.9 | [Version number](http://www.apache.org/dist/maven/)|
| maven_home_parent_directory | /opt | MAVEN_HOME parent directory|

Installation
------------

 `$ ansible-playbook -c local maven.yml`

Example Playbook
----------------
```
 - hosts: localhost
   roles:
     - { role: apache-maven }
```
