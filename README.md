OneInstance
===========

Description
-----------

A Java library which can be used to prevent the launch of multiple
instances of an application. The first instance is informed about the launch
of a second one, can decide if the second instance is allowed to start and
also gets the working directory and the command-line arguments of the second 
instance.

The communication between the application instances is done via a TCP socket
server which is started on the loop-back interface by the first instance. The
port is chosen automatically from the range 49152 to 65535. The port is
stored in the Java preferences so additional application instances know where
the first instance might be listening (If it is still running). The communication
is authenticated with an application ID (The fully qualified name of the 
applications main class) to prevent accidental communication with other
applications which may also use OneInstance or which are completely unrelated.  


License
-------

Copyright (C) 2012 Klaus Reimer <k@ailis.de>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and to permit persons to whom the Software is furnished to do
so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
SOFTWARE.


Download
--------

The JAR can be downloaded from my [Maven Repository][1] or if you are
using Maven you can simply add it as a dependency:

    <repositories>
      <repository>
        <id>ailis-releases</id>
        <name>Ailis Maven Releases</name>
        <url>http://nexus.ailis.de/content/groups/public/</url>
      </repository>
    </repositories>

    <dependencies>
      <dependency>   
        <groupId>de.ailis.oneinstance</groupId>
        <artifactId>oneinstance</artifactId>
        <version>1.0.1</version>
      </dependency>
    </dependencies>


Usage
-----

    public static void main(String[] args)
    {
        OneInstance oneInstance = OneInstance.getInstance();
        
        // Install listener which processes the start of secondary instances
        oneInstance.addListener(new OneInstanceListener()
        {
            public boolean newInstanceCreated(File workingDir, String[] args)
            {
                ... Process command line args of the other instance ...
                ... workingDir can be used to resolve relative filenames ...
                
                // Tell the other instance to exit
                return false;
            }
        });       
    
        // Exit the application if we are NOT the first instance and the
        // real first instance decided that there can be only ONE instance.
        if (!oneInstance.register(Main.class, args)) System.exit(0);
        
        ... Continue with your application ...               
    }

[1]: http://nexus.ailis.de/content/repositories/releases/de/ailis/oneinstance/oneinstance/ "Maven Repository"
