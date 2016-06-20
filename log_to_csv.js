var fileInput = document.createElement("INPUT");
fileInput.type = "file";
fileInput.onchange = handleFiles;
fileInput.click();
function handleFiles(e){  if (this.files && this.files[0])  {fileToText(this.files[0]);}}
function fileToText(file){
  var reader = new FileReader();
  reader.onload = function(){parseLines(reader.result);};
  reader.readAsText(file);}
function parseLines(unproccessedText){
  var text = unproccessedText.replace(/^/gm, "Day=").replace(/:/gm, "");
  var output = header().join() + "\n" + text.split("\n").map(parseLine).join("\n");
  location.href = "data:text/csv," + encodeURI(output);
  function splitIntoWords(text){return text.split(/\s/);}
  function header(){
   function takeKey(word){return word.split("=")[0];}
   keys = splitIntoWords(text).map(takeKey).filter(isNotEmpty);
   function isNotEmpty(text){return text.length !== 0;}
   return Array.from(new Set(keys));}
  function parseLine(line){
    return header().map(findValueInLine);
    function findValueInLine(key){
      var filteredLine = line.split(/\s/).filter(testWord).map(takeValue);
      return filteredLine[0];
      function takeValue(word){return word.split("=")[1];}
      function testWord(word){return word.search(key)!==-1;}}}}
