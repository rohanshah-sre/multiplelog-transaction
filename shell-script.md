# Reviewing Transactions & Common Data Across Multiple Log Files
<!-- ------------------------ -->
## Overview 


### What You’ll Learn Today
In this lab we'll simulate a transaction through multiple logs and review the logs and specific transactions, via transaction IDs, through Dynatrace DQL. This lab will provide a quick setup of a cron job that with generate 6 log files in 3 different formats. This is useful for anyone looking to find commmonality of specfic values across multiple log files.

### In this lab we will...
1. Setup a log simulation shell script
1. Deploy Dynatrace OneAgent 
1. Setup an cronjob on a Linux VM
1. Review the generated log entries in Dynatrace
1. Learn basic parsing syntax for Dynatrace DQL 

![such dql, much wow!](img/1.png)


<!-- ------------------------ -->
## Technical Specification 


### Technologies We Will Work With Today
- [Dynatrace SasS Tenant](https://www.dynatrace.com/trial/)
- [Crontab](https://man7.org/linux/man-pages/man5/crontab.5.html)
- [Bash Shell](https://en.wikipedia.org/wiki/Bash_(Unix_shell))
- [JSON](https://www.w3schools.com/js/js_json_syntax.asp)
- [XML](https://www.w3schools.com/xml/)
- [GIT](https://git-scm.com/)
- [Dynatrace Query Language (DQL)](https://docs.dynatrace.com/docs/platform/grail/dynatrace-query-language)

### Simulated Transaction Flow 
 Some code or apis will take an input from a end user or an api, like a user name, password, credit card transaction, UUID, etc and pass that input as a variable between other apis and down stream services. Sometimes, these values are also logged for debugging or reporting purposes. For the purposes of this lab, the script we will deploy will use set of templates to dynamically populate a set of "transaction" IDs across six seperate log files or "devices" to simulate this type of transaction. We'll add some complexity to this transaction flow by having a differnt logging format for each "device" or "service" hop. We'll use The Dynatrace Query Language to quickly and easily access, review and track specific transactions as they run through our environment:

![transaction flow](img/2.png)


### Log Generation
Within the [github repo](https://github.com/kyledharrington/multiplelog-transaction) we'll leverage the `multi-logs.sh` script. 

When run, this script will generate a few diffent variables: 

- The path to logs is set to via
`directory_path="/var/log/multilog"`

    This can be be updated as desired

-  Two specific "transaction IDs", the `dttransid` and the `pmdwid`

    ```
    dttransid=$(date +%Y%m%d)DYNATRANSACTION$RANDOM
        example: 20240624DYNATRANSACTION10198

    pmdwid=$(mktemp -u XXXXXXXXXXXXXXX)
        example: jG3I5VbKOgENt3F`
    ```
With these variables set, the script will then leverage log output templates in the `/templates` directory to read, append the varibles in those templates and generate a new log line in each of the 6 log files created. Each log line is also setting a variable via the `dynatime` variable to generate a timestamp. We will also use the script `sleep` function to mimic latency between our device hops. 

The end result will be 6 logs for our "transaction processors" in the `/var/log/multilog` directory

![logs files](img/3.png)

<!-- -------------------------->
## SETUP

![For the codespaces version of this this lab see the codespaces setup](/README.md)


###
[Video Recording for Setup can be view here](https://dynatrace-my.sharepoint.com/:v:/p/kyle_harrington/EaPhCVPUEohHprzGBsk3NQ8BupqPUs9s1NlbmYp75n29HQ?nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJPbmVEcml2ZUZvckJ1c2luZXNzIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXciLCJyZWZlcnJhbFZpZXciOiJNeUZpbGVzTGlua0NvcHkifX0&e=eqrnx1)

### Prerequisites
1. A active Dynatrace SaaS tenant
    - If you don't have one you can sign up for a free 15 day [Dynatrace Trial](https://www.dynatrace.com/signup/)
1. A linux based virtual machine 
    - If you don't have one, GCP offers a $300 trial credit for new users [Google Cloud](https://cloud.google.com/)
1. [Dynatrace OneAgent Installed on the Linux VM](https://docs.dynatrace.com/docs/setup-and-configuration/dynatrace-oneagent/installation-and-operation/linux/installation/install-oneagent-on-linux)

### Setup the scipt

1. Clone the git repo to your home directory
    ```shell
    git clone https://github.com/kyledharrington/multiplelog-transaction.git 
    ```
1. Modify your user's contab:
    ```
    crontab -e
    ```
1. Add the script to your crontab: 
    ```
    * * * * * /home/$(whoami)/multiplelog-transaction/multi-logs.sh
    ```

1. Save and quit with:
    ```
    :wq!
    ```
    ![logs files](img/cron.gif)



    The `* * * * *` will execute the script every minute. We'll let let the script run for a few minutes to generate data.

1. upload the `multitple-log-transaction-notebook.json` notebook to you Dynatrace tenant
    - this file is in the "assets" folder of this repository
    - [Click here](/assets/)


    ![logs files](img/upload.gif)

We will continue the rest of the lab in the `lab-multitple-log-transaction-notebook.json` notebook

** TO DO: DEMO VIDEO ** 