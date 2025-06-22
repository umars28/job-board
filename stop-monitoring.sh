#!/bin/bash

# === STOP PROMETHEUS ===
if [ -f prometheus.pid ]; then
  PROM_PID=$(cat prometheus.pid)
  echo "Stopping Prometheus (PID: $PROM_PID)..."
  kill $PROM_PID
  rm prometheus.pid
else
  echo "No prometheus.pid found. Is Prometheus running?"
fi

# === STOP GRAFANA ===
if [ -f grafana.pid ]; then
  GF_PID=$(cat grafana.pid)
  echo "Stopping Grafana (PID: $GF_PID)..."
  kill $GF_PID
  rm grafana.pid
else
  echo "No grafana.pid found. Is Grafana running?"
fi

echo "✅ Prometheus & Grafana stopped!"
