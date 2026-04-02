// 后端目前没有 /images 前缀（@RestController("/images") 不会生效为路径前缀）
// 所以这里直接调用 /upload 与 /list
const API_BASE = "";

const uploadForm = document.getElementById("uploadForm");
const fileInput = document.getElementById("fileInput");
const uploadBtn = document.getElementById("uploadBtn");
const statusEl = document.getElementById("status");
const galleryEl = document.getElementById("gallery");

function setStatus(text) {
  statusEl.textContent = text || "";
}

function createCard(item) {
  const card = document.createElement("div");
  card.className = "card";

  const img = document.createElement("img");
  img.className = "thumb";
  img.src = item.ossUrl;
  img.alt = item.fileName || "image";

  const meta = document.createElement("div");
  meta.className = "meta";
  meta.textContent = item.fileName || "";

  const actions = document.createElement("div");
  actions.className = "actions";

  const openLink = document.createElement("a");
  openLink.href = item.ossUrl;
  openLink.target = "_blank";
  openLink.rel = "noopener noreferrer";
  openLink.textContent = "打开查看";

  actions.appendChild(openLink);
  card.appendChild(img);
  card.appendChild(meta);
  card.appendChild(actions);
  return card;
}

async function loadList() {
  setStatus("加载中...");
  galleryEl.innerHTML = "";

  const res = await fetch(`${API_BASE}/list`, { method: "GET" });
  if (!res.ok) {
    const txt = await res.text().catch(() => "");
    throw new Error(`GET ${API_BASE}/list failed: ${res.status} ${txt}`);
  }

  const list = await res.json();
  if (!Array.isArray(list) || list.length === 0) {
    setStatus("暂无图片。");
    return;
  }

  for (const item of list) {
    galleryEl.appendChild(createCard(item));
  }
  setStatus(`已加载 ${list.length} 条记录。`);
}

uploadForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  const file = fileInput.files && fileInput.files[0];
  if (!file) return;

  uploadBtn.disabled = true;
  setStatus("上传中...");

  try {
    const formData = new FormData();

    // 兼容后端参数名（你的控制器参数名是 multipartfile）
    formData.append("multipartfile", file);
    // 兼容少数前端/示例习惯字段名
    formData.append("file", file);

    const res = await fetch(`${API_BASE}/upload`, {
      method: "POST",
      body: formData
    });

    if (!res.ok) {
      const txt = await res.text().catch(() => "");
      throw new Error(`POST ${API_BASE}/upload failed: ${res.status} ${txt}`);
    }

    const saved = await res.json();
    setStatus(`上传成功：\n${saved.fileName}\n${saved.ossUrl}`);

    await loadList();
  } catch (err) {
    setStatus(`上传失败：\n${err.message}`);
  } finally {
    uploadBtn.disabled = false;
  }
});

loadList().catch((e) => setStatus(e.message));

