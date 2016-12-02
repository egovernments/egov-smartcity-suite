PostgreSQL role for Ansible
===========================

A role for deploying and configuring [PostgreSQL](http://www.postgresql.org/) and extensions on unix hosts using [Ansible](http://www.ansibleworks.com/).

It can additionally be used as a playbook for quickly provisioning hosts.

Vagrant machines are provided to produce a boxed install of PostgreSQL or a VM for integration testing.


Supports
--------
Supported PostgreSQL versions:

- PostgreSQL 9.5
- PostgreSQL 9.4
- PostgreSQL 9.3

Supported targets:
- Ubuntu 14.04 LTS "Trusty Tahr"
- Ubuntu 12.04 LTS "Precise Pangolin"
- Debian (untested)
- RedHat (untested)

Installation methods:

- Binary packages from the official repositories at [postgresql.org](http://www.postgresql.org/download/)

Available extensions (under a switch):

- Development headers - `postgresql_dev_headers`
- Contrib modules - `postgresql_contrib`
- [PostGIS](http://postgis.net/) - `postgresql_postgis`


Usage
-----
Clone this repo into your roles directory:

    $ git clone https://github.com/zenoamaro/ansible-postgresql.git roles/postgresql

And add it to your play's roles:

    - hosts: ...
      roles:
        - postgresql
        - ...

This roles comes preloaded with almost every available default. You can override each one in your hosts/group vars, in your inventory, or in your play. See the annotated defaults in `defaults/main.yml` for help in configuration. All provided variables start with `postgresql_`.

You can also use the role as a playbook. You will be asked which hosts to provision, and you can further configure the play by using `--extra-vars`.

    $ ansible-playbook -i inventory --extra-vars='{...}' main.yml

To provision a standalone PostgreSQL box, start the `boxed` VM, which is a Ubuntu 12.04 box:

    $ vagrant up boxed

You will find PostgreSQL listening on the VM's `5432` port on address `192.168.33.20` in the private network. You can then connect to it as any user. Please note that this is highly insecure, so if you're going to publish this VM you'll want to provide actual authentication.

Run the tests by provisioning the appropriate VM:

    $ vagrant up test-ubuntu-trusty

At the moment, the following test boxes are available:

- `test-ubuntu-precise`
- `test-ubuntu-trusty`


Still to do
-----------
- Add repositories, tasks and test VMs for other distros
- Allow installation from sources if requested
- Add support for different PostgreSQL versions (9.4+)
- Add support for PostgreSQL clusters
- Add support for [PgBouncer](http://wiki.postgresql.org/wiki/PgBouncer)
- Try to make the boxed VM more secure by default
- Add links to the relevant documentation in configuration files
- Provide a library of custom modules


Changelog
---------
### 0.2.0
- Updated default version to PostgreSQL 9.5.
- Removed unsupported `checkpoint_segments` configuration
- Removed unsupported `ssl_renegotiation_limit` configuration

### 0.1.2
- Installing less dependencies, and later in the process.

### 0.1.1
- The package list is not being updated in playbooks anymore.
- Added more test machines.

### 0.1
Initial version.


License
-------
The MIT License (MIT)

Copyright (c) 2016, zenoamaro <zenoamaro@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
