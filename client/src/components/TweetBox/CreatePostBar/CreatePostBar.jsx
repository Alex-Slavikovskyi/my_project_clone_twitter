import React from 'react';
import AddImage from './BarItem/AddImage';
import Emoji from './BarItem/Emoji';
import Gif from './BarItem/Gif';
import Schedule from './BarItem/Schedule';
import Location from './BarItem/Location';
import style from './CreatePostBar.module.scss';

function CreatePostBar() {
  return (
    <div className={style.iconsRow}>
      <div>
        <AddImage />
      </div>
      <div>
        <Gif />
      </div>
      <div>
        <Emoji />
      </div>
      <div>
        <Schedule />
      </div>
      <div>
        <Location />
      </div>
    </div>
  );
}

export default CreatePostBar;