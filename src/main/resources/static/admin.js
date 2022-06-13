function openData(evt, dataName) {
      // Declare all variables
      var i, tabcontent, tablinks;

      // Get all elements with class="tabcontent" and hide them
      tabcontent = document.getElementsByClassName("tabcontent");
      for (i = 0; i < tabcontent.length; i++) {
          tabcontent[i].style.display = "none";
      }

      // Get all elements with class="tablinks" and remove the class "active"
      tablinks = document.getElementsByClassName("tablinks");
      for (i = 0; i < tablinks.length; i++) {
          tablinks[i].className = tablinks[i].className.replace(" active", "");
      }

      // Show the current tab, and add an "active" class to the button that opened the tab
      document.getElementById(dataName).style.display = "block";
      evt.currentTarget.className += " active";
  }

  document.addEventListener('DOMContentLoaded', () => {

      const getSort = ({ target }) => {
          const order = (target.dataset.order = -(target.dataset.order || -1));
          const index = [...target.parentNode.cells].indexOf(target);
          const collator = new Intl.Collator(['en', 'ru'], { numeric: true });
          const comparator = (index, order) => (a, b) => order * collator.compare(
              a.children[index].innerHTML,
              b.children[index].innerHTML
          );

          for(const tBody of target.closest('table').tBodies)
              tBody.append(...[...tBody.rows].sort(comparator(index, order)));

          for(const cell of target.parentNode.cells)
              cell.classList.toggle('sorted', cell === target);
      };

      document.querySelectorAll('.users thead').forEach(tableTH => tableTH.addEventListener('click', () => getSort(event)));

  });
