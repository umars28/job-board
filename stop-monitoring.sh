#!/bin/bash

echo "Stopping Prometheus..."
pkill -f prometheus

echo "Stopping Grafana..."
pkill -f grafana

echo "✅ Prometheus & Grafana stopped!"
