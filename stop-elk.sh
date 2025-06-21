#!/bin/bash

echo "Stopping Elasticsearch..."
pkill -f elasticsearch

echo "Stopping Logstash..."
pkill -f logstash

echo "Stopping Kibana..."
pkill -f kibana

echo "✅ All ELK services have been stopped."
