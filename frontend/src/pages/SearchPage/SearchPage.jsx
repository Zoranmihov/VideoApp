import React from 'react';
import "./searchPage.css"

import SearchUsers from '../../componnents/SearchUsers/SearchUsers';
import SearchVideos from '../../componnents/SearchVideos/SearchVideos';

export default function SearchPage({ resetSearch }) {
  let searchTerm = window.location.href.split("/");
  searchTerm = searchTerm[searchTerm.length - 1]

  return (
    <>
      <SearchUsers searchTerm={searchTerm} />
      <SearchVideos searchTerm={searchTerm} resetSearch={resetSearch} />
    </>
  );
}
