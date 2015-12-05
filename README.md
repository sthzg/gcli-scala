# gcli-scala
Command Line Search Client for Google


**What**  
–––

`Gcli` aims to bring otherwise *hard-to-reach* Google search filters to the command line. I am frequently only 
interested in finding current content. Google provides the time-based search filters, but AFAIK only through the UI.

**Current state of affairs:**

    ▶ java -jar ./gcli-scala-assembly-0.1.jar --help
    gcli 0.1
    Usage: gcli [options] <query terms>...
    
      --help
            prints this usage text
      <query terms>...
            search query
      -q <value> | --quick <value>
            quick options: last y(ear) | m(onth) | d(ay) | w(eek) | h(hour)
      --yfrom <value>
            search for content more recent than --yfrom (accepts 2-digit and 4-digit numbers)
      --yto <value>
            search for content older --yto (accepts 2-digit and 4-digit numbers)
      --noop
            run program but don't open the browser


**From within SBT**: (`--noop` prevents actually opening the browser)

    > run -q:y I really need to find this                            # last year
    Searching for: I really need to find this
    noop: would run open https://www.google.com/?#q=I+really+need+to+find+this&tbs=qdr:y

    > run I really need to find this -q m                            # last month
    Searching for: I really need to find this
    noop: would run open https://www.google.com/?#q=I+really+need+to+find+this&tbs=qdr:m

    > run and now for something different --yfrom:2013               # later than 2013
    Searching for: and now for something different
    noop: would run open https://www.google.com/?#q=and+now+for+something+different&tbs=cdr:1,cd_min:2013,cd_max:
    
    > run and now for something different --yto:2013                 # older than 2013
    Searching for: and now for something different
    noop: would run open https://www.google.com/?#q=and+now+for+something+different&tbs=cdr:1,cd_min:,cd_max:2013
    
    > run and now for something different --yfrom:2010 --yto:2013    # between 2010 and 2013
    Searching for: and now for something different
    noop: would run open https://www.google.com/?#q=and+now+for+something+different&tbs=cdr:1,cd_min:2010,cd_max:2013
    
    
**Configuration**  
–––

`WIP` When you run the command, you will be prompted for approval to create a `.gcli` directory 
at your home dir. If you approve a `config.json` will be saved in that directory. There is currently no nice api 
to write the config file, but you could open it up in your editor and modify these settings:

    # vi ~/.gcli/config.json
    startCommand ... the command that will be invoked in your shell to open the browser (default: open)
    tld ............ the Google top-level domain to use for your search (default: com)
    
`open` works on Mac OS X, but you could change it for example to invoke `open -a firefox` or something similar. On 
other platforms another command might be needed, e.g. `xdg-open` on Linux.

The reason for making `.gcli` a directory rather than a dot-file is that this directory will also be the storage 
location for search profiles and other user settings in the future.

In a future version the path itself will become configurable, so that it gets possible to store these files in 
Dropbox, Livedrive, etc.

**Caution** the `startCommand` is currently executed as is, so if someone tries to do some `sudo rm -Rfv / && open` 
 in that setting that might become a sad moment.

    
**Running from CLI**  
–––

Executing by typing `$ java -jar ...` is cumbersome. On Unix/Linux it is easy to get around with either an alias 
or a shell script. An alias in a dot-file like `.bashrc` might look like.

    alias gcli='java -jar /path/to/gcli-scala.jar'


**More interesting stuff** (*that might be added*)  
–––

- [ ] Make open command customizable
- [ ] Make Google's TLD customizable 
- [ ] Provide option to limit searches to domain (e.g. stackoverflow)
- [ ] Provide feature to save search presets (i.e. `--preset:foo`)
- [ ] Make default search settings customizable (behavior /wo any options)
- [ ] Make UI for interactive mode (`-i`)
- [ ] Support image and video search
- [ ] Show search results on command line?


**Dependencies**  
–––

- [scopt](https://github.com/scopt/scopt)


**Motivation**  
–––

> This is a fun and educational project to get to know the world of Scala. Nevertheless it may become a useful utility 
in a developer's toolbox.


**License**  
––

MIT
