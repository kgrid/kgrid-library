The ObjectTeller application uses a local MySQL database for managing user info and library connection info.

One way to get MySQL up and running is to download and install a recent version. You can download manually, or use homebrew on a Mac. (In the end this is what I chose.)

Another is to install a virtually machine preconfigured w/ MySQL, using VirtualBox and/or vagrant. This probably requires some kind of SSH tunnelling/port forwarding to get it to work on `localhost:3306`. Here's a hint on setting it up with vagrant, https://gielberkers.com/create-mysql-ssh-tunnel-within-vagrant/. (I tried out the Scotch Box vagrant LAMP box.)


Object IDs:

If you are running a local instance of the ObjectTeller and pointing to a remote instance of a (Fedora) library, you need to make sure you don't have object id collisions. There is an initial object id set at the end of the db create script that needs to be in a range outside of the library's current range. Since the library started low, I set mine to 10,000, which worked fine.



