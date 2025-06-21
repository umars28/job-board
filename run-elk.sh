#!/bin/bash

# -----------------------------------------------
# Script to run Elasticsearch, Logstash, Kibana, Filebeat in parallel on macOS
# -----------------------------------------------

# Absolute Paths
FILEBEAT_DIR="/Users/umarsabirin/Documents/ELK-Installed/filebeat-8.18.1-darwin-x86_64"
LOGSTASH_DIR="/Users/umarsabirin/Documents/ELK-Installed/logstash-8.18.1"
KIBANA_DIR="/Users/umarsabirin/Documents/ELK-Installed/kibana-8.18.1"
ELASTICSEARCH_DIR="/Users/umarsabirin/Documents/ELK-Installed/Elasticsearch-installed/elasticsearch-8.18.1"

# Start Elasticsearch
echo "🚀 Starting Elasticsearch..."
(cd "$ELASTICSEARCH_DIR" && bin/elasticsearch) &

# Wait for Elasticsearch to be up before starting others
sleep 10

# Start Logstash
echo "🚀 Starting Logstash..."
(cd "$LOGSTASH_DIR" && bin/logstash -f "/Users/umarsabirin/Documents/Self Projects/SPRING BOOT/job-boards/logstash.conf") &

# Start Kibana
echo "🚀 Starting Kibana..."
(cd "$KIBANA_DIR" && bin/kibana) &

# Start Filebeat
echo "🚀 Starting Filebeat..."
(cd "$FILEBEAT_DIR" && ./filebeat -e -c "/Users/umarsabirin/Documents/Self Projects/SPRING BOOT/job-boards/filebeat.yml") &

# -----------------------------------------------
# Keep script alive
# -----------------------------------------------
wait
