
function pad(n) { return n < 10 ? '0'+n : n; }

function toggleNow(checkbox) {
    const dtInput = document.getElementById('datetimeInput');
    if (checkbox.checked) {
        const now = new Date();
        // Muotoilu: YYYY-MM-DDTHH:MM
        const yyyy = now.getFullYear();
        const mm = pad(now.getMonth() + 1);
        const dd = pad(now.getDate());
        const h = pad(now.getHours());
        const m = pad(now.getMinutes());
        const dtString = `${yyyy}-${mm}-${dd}T${h}:${m}`;
        dtInput.value = dtString;
        dtInput.readOnly  = true;
    } else {
        dtInput.readOnly  = false;
        dtInput.value = '';
    }
}



function renderTable(data) {
    const tbody = document.querySelector("#logTable tbody");
    tbody.innerHTML = '';
    data.forEach(row => {
    const tr = document.createElement('tr');
    const lat = row.latitude;
    const lon = row.longitude; 
    tr.innerHTML = `
        <td>${row.time.replace('T',' ').substring(0,16)}</td>
        <td>${row.title}</td>
        <td>${row.body}</td>
        <td>${lat !== null && !isNaN(Number(lat)) ? Number(lat).toFixed(4) : ''}</td>      
        <td>${lat !== null && !isNaN(Number(lon)) ? Number(lon).toFixed(4) : ''}</td>   
        <td>
            <button class="delete" onclick="deleteRow(${row.id})">Poista</button>
        </td>
    `;
    tbody.appendChild(tr);
  });
}

async function fetchData() {
    const res = await fetch("/entries");
    let data = await res.json();
    updateTime();
    renderTable(data);
    renderMarkers(data);
}
function renderMarkers(data) {
    window.markers.clearLayers();
    let bounds = null;

    const n = data.length;
    console.log(Array.isArray(data));
    data.forEach(row => {
        const lat = row.latitude;
        const lon = row.longitude;
        if (lat !== null && lon !== null) {
            const latlng = L.latLng(Number(lat), Number(row.longitude));
            if (!bounds) {
              bounds = L.latLngBounds(latlng, latlng);
            } else {
              bounds.extend(latlng);
            }
            L.marker([Number(lat), Number(lon)])
                .addTo(window.markers) 
                .bindTooltip(row.title, { permanent: true, direction: "top" });
        }
    });

    if (bounds) {
      window.map.fitBounds(bounds, {padding: [30, 30]});
    }
}


function deleteRow(data){
    const deleted = fetch("/entries/"+data, {
        method: "DELETE"
    });
    fetchData();
}


function updateTime(){
    const checkbox = document.getElementById('nowCheckbox');
    const dtInput = document.getElementById('datetimeInput');
    if (checkbox.checked){
        const now = new Date();
        const yyyy = now.getFullYear();
        const mm = pad(now.getMonth() + 1);
        const dd = pad(now.getDate());
        const h = pad(now.getHours());
        const m = pad(now.getMinutes());
        const dtString = `${yyyy}-${mm}-${dd}T${h}:${m}`;
        dtInput.value = dtString;
    }
}

// Automaattinen päivitys
setInterval(fetchData, 5000);
fetchData();

