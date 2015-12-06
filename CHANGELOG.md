- 2015-12-06 **refactored app structure**  
Split app in more parts to allow easier testing of the commands that are assembled by 
parsing the CL arguments.
- 2015-12-06 **added --noconf flag**  
Allows to skip prompting for a config.json and using defaults instead
- 2015-12-06 **added --so flag**  
Use --so flag to limit search results to stackoverflow.com
- 2015-12-06 **added --site option**  
Use --site theguardian.com to limit search results to a domain
- 2015-12-05 **added config file**  
Added rudimentary support for an application config file located at `~/.gcli/config.json`.
- 2015-12-05 **integrated with springtest**  
