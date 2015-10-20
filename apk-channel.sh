  #!/bin/bash

if [ -f channel.txt ]
then
	####pre_package begin####
    ant clean -buildfile build-lezyo-fsm4mac.xml
	ant pre_package -buildfile build-lezyo-fsm4mac.xml
	####pre_package end  ####
	while read channel release_first
	do
		ant release_channel -buildfile build-lezyo-fsm4mac.xml -Dumeng_channel="${channel}_market"  -Ddebug_switch=no -Dumeng_switch=yes -Dfirst_release="${release_first}" -Dis_release=1
    done < channel.txt
else
	echo "渠道文件不存在!"
fi
