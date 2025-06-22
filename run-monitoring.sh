#!/bin/bash

# === CONFIG ===
PROMETHEUS_DIR="/Users/umarsabirin/Documents/Monitoring Tools/prometheus-3.4.1.darwin-amd64"
GRAFANA_DIR="/Users/umarsabirin/Documents/Monitoring Tools/grafana-v12.0.0+security-01"

PROMETHEUS_CONFIG="/Users/umarsabirin/Documents/Self Projects/SPRING BOOT/job-boards/prometheus.yml"

# === START PROMETHEUS ===
echo "Starting Prometheus..."
cd "$PROMETHEUS_DIR"
./prometheus --config.file="$PROMETHEUS_CONFIG" > prometheus.log 2>&1 &
echo $! > prometheus.pid

# === START GRAFANA ===
echo "Starting Grafana..."
cd "$GRAFANA_DIR"
./bin/grafana-server web > grafana.log 2>&1 &
echo $! > grafana.pid

echo "✅ Prometheus & Grafana started!"
