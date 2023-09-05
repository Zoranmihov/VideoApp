export async function fetchData(method, url, object) {
    try {
      const response = await fetch(url, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(object),
      });
  
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
  
      return await response.json();
  
    } catch (error) {
      console.error('There was a problem with the fetch operation:', error.message);
      throw error; // Re-throw the error so it can be caught and handled by the calling function if needed
    }
  }