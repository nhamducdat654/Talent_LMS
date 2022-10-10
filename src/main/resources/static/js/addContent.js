document.getElementById("txtUnitName").addEventListener("input", () => {
  let count = document.getElementById("txtUnitName").value;
  let range = 80;
  document.getElementById("rangeInput").value = range - count.length;
});
