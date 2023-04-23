#!/bin/bash
mvn clean package wagon:upload-single wagon:sshexec
