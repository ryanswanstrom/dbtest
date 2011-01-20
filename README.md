This a sample app built with the Play Framework to test the speed of inserts and queries of both [MongoHQ](http://mongohq.com) and [Xeround](http://xeround.com)

Prereqs
=======

1. Installed mongodb locally and running
1. Play Framework installed
1. Modules added: morphia, secure, stax

To Run
=====

1. git clone https://github.com/swGooF/dbtest.git
1. cd dbtest
1. play secret
1. play run

To Deploy
=====

1. Update conf/application.conf with the correct values for the MySQL DB and MongoDB
1. cd ..
1. stax create yourproject
1. play war ./dbtest/ -o staxdbtest --zip
1. stax app:deploy -a username/yourproject staxdbtest.war 
