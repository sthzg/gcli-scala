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

    > run I really need to find this -q y
    Searching for: I really need to find this
    noop: would run open https://www.google.com/?#q=I+really+need+to+find+this&tbs=qdr:y

    > run I really need to find this -q m
    Searching for: I really need to find this
    noop: would run open https://www.google.com/?#q=I+really+need+to+find+this&tbs=qdr:m

    > run and now for something different --yfrom:2013
    Searching for: and now for something different
    noop: would run open https://www.google.com/?#q=and+now+for+something+different&tbs=cdr:1,cd_min:2013,cd_max:
    
    > run and now for something different --yto:2013
    Searching for: and now for something different
    noop: would run open https://www.google.com/?#q=and+now+for+something+different&tbs=cdr:1,cd_min:,cd_max:2013
    
    > run and now for something different --yfrom:2010 --yto:2013
    Searching for: and now for something different
    noop: would run open https://www.google.com/?#q=and+now+for+something+different&tbs=cdr:1,cd_min:2010,cd_max:2013


**More interesting stuff** (*that might be added*)  
–––

- [ ] Make open command customizable
- [ ] Provide tests
- [ ] Make Google's TLD customizable 
- [ ] Explore ways to provide nicer executables (not much fun with `java -jar ./gcli`) 
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
