export const calculateDaysAgo = (uploadedAt) => {
    const uploadDate = new Date(uploadedAt);

    const currentDate = new Date();

    const diffInMilliseconds = currentDate - uploadDate;

    const diffInDays = Math.floor(diffInMilliseconds / (1000 * 60 * 60 * 24));

    if (diffInDays < 1) {
      return "Today";
  } else if (diffInDays === 1) {
      return `${diffInDays} day ago`;
  } else {
      return `${diffInDays} days ago`;
  }

  }