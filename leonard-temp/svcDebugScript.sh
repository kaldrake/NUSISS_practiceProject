#!/bin/bash
# Run this inside your container to debug services
echo "=== Service Debug Report ==="
echo "Timestamp: $(date)"
echo ""

echo "=== Running Processes ==="
ps aux | grep -E "(nginx|java|mysql)" | grep -v grep
echo ""

echo "=== Port Status ==="
netstat -tlnp 2>/dev/null | grep -E ":(80|8080|3306)" || echo "netstat not available, trying ss:"
ss -tlnp 2>/dev/null | grep -E ":(80|8080|3306)" || echo "ss not available"
echo ""

echo "=== Nginx Status ==="
if command -v nginx &> /dev/null; then
    nginx -t 2>&1
    if pgrep nginx > /dev/null; then
        echo "✅ Nginx is running"
    else
        echo "❌ Nginx is not running"
    fi
else
    echo "❌ Nginx not installed"
fi
echo ""

echo "=== Test Internal Connections ==="
echo "Testing Spring Boot (port 8080):"
curl -s -o /dev/null -w "HTTP Status: %{http_code}\n" http://localhost:8080/api/actuator/health || echo "Connection failed"

echo "Testing Nginx (port 80):"
curl -s -o /dev/null -w "HTTP Status: %{http_code}\n" http://localhost:80/ || echo "Connection failed"
echo ""

echo "=== File System Check ==="
echo "Spring Boot JAR exists: $([ -f /app/app.jar ] && echo "✅ YES" || echo "❌ NO")"
echo "Frontend files exist: $([ -d /var/www/html ] && echo "✅ YES" || echo "❌ NO")"
echo "Frontend index.html: $([ -f /var/www/html/index.html ] && echo "✅ YES" || echo "❌ NO")"
echo "Nginx config exists: $([ -f /etc/nginx/sites-available/default ] && echo "✅ YES" || echo "❌ NO")"
echo ""

echo "=== Nginx Configuration ==="
if [ -f /etc/nginx/sites-available/default ]; then
    echo "Current Nginx config:"
    cat /etc/nginx/sites-available/default
else
    echo "❌ Nginx config file not found"
fi